import { serve } from "https://deno.land/std@0.168.0/http/server.ts"
import { createClient } from "https://esm.sh/@supabase/supabase-js@2"
import { JWT } from "https://esm.sh/google-auth-library@9"

// Provide standard CORS headers
const corsHeaders = {
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Headers': 'authorization, x-client-info, apikey, content-type',
}

serve(async (req) => {
  // Handle CORS preflight requests
  if (req.method === 'OPTIONS') {
    return new Response('ok', { headers: corsHeaders })
  }

  try {
    // Parse the payload sent by the Database Webhook (it sends the inserted row)
    const payload = await req.json()
    const emergency = payload.record // This is the newly inserted emergency

    if (!emergency || !emergency.city) {
      return new Response(JSON.stringify({ error: "Invalid payload or missing location" }), {
        headers: { ...corsHeaders, 'Content-Type': 'application/json' },
        status: 400,
      })
    }

    // Initialize Supabase Client with service role to bypass RLS
    const supabaseClient = createClient(
      Deno.env.get('SUPABASE_URL') ?? '',
      Deno.env.get('SUPABASE_SERVICE_ROLE_KEY') ?? ''
    )

    // 1. Fetch donors in the exact same location who have an FCM token
    const { data: donors, error: donorError } = await supabaseClient
      .from('donors')
      .select('fcm_token')
      .eq('city', emergency.city)
      .eq('district', emergency.district)
      .not('fcm_token', 'is', null)

    if (donorError) throw donorError

    if (!donors || donors.length === 0) {
      return new Response(JSON.stringify({ message: "No donors found in this location" }), {
        headers: { ...corsHeaders, 'Content-Type': 'application/json' },
        status: 200,
      })
    }

    // Extract the FCM tokens
    const tokens = donors.map((d: any) => d.fcm_token)

    // 2. Setup Google Auth to get an Access Token using the Service Account JSON
    // The service account JSON string is stored in a Supabase Secret named 'FIREBASE_SERVICE_ACCOUNT'
    const serviceAccountStr = Deno.env.get('FIREBASE_SERVICE_ACCOUNT')
    if (!serviceAccountStr) {
      throw new Error("Missing FIREBASE_SERVICE_ACCOUNT secret.")
    }
    
    const serviceAccount = JSON.parse(serviceAccountStr)
    
    // Create JWT Client
    const jwtClient = new JWT({
      email: serviceAccount.client_email,
      key: serviceAccount.private_key,
      scopes: ['https://www.googleapis.com/auth/firebase.messaging'],
    })

    // Get the OAuth2 Access Token
    const tokensResult = await jwtClient.getAccessToken()
    const accessToken = tokensResult.token

    if (!accessToken) {
      throw new Error("Failed to retrieve access token from Google.")
    }

    // 3. Send the notification via FCM v1 API
    const projectId = serviceAccount.project_id
    const fcmEndpoint = `https://fcm.googleapis.com/v1/projects/${projectId}/messages:send`

    // We will send notifications individually or use a multicast loop
    const notifications = tokens.map(async (token: string) => {
      const response = await fetch(fcmEndpoint, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          message: {
            token: token,
            notification: {
              title: "URGENT: Blood Needed in Your Area!",
              body: `An emergency for ${emergency.blood_type} blood has been posted at ${emergency.hospital_name}.`,
            },
            data: {
              emergency_id: emergency.id,
              click_action: "FLUTTER_NOTIFICATION_CLICK" // Generic action, works for Android Native too
            }
          }
        })
      })

      return response.json()
    })

    const results = await Promise.all(notifications)

    return new Response(JSON.stringify({ success: true, results }), {
      headers: { ...corsHeaders, 'Content-Type': 'application/json' },
      status: 200,
    })

  } catch (error) {
    console.error("Error sending notification:", error)
    return new Response(JSON.stringify({ error: error.message }), {
      headers: { ...corsHeaders, 'Content-Type': 'application/json' },
      status: 500,
    })
  }
})
