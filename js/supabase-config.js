// ============================================
// VitalMatch - Supabase & Location API Configuration
// ============================================

const SUPABASE_URL = 'https://wcoipyffkhhvcpknrqpb.supabase.co';
const SUPABASE_ANON_KEY = 'sb_publishable_dE6gJYL2yGgxS9lIEKoqHA_FDooPDHg';

// Gemini / Google Location Geocoding API Key
const LOCATION_API_KEY = 'AIzaSy_GEMINI_LOCATION_GEOCODING_KEY_VITALMATCH';

// Initialize client
const supabaseClient = window.supabase.createClient(SUPABASE_URL, SUPABASE_ANON_KEY);

// Auth helpers
async function supabaseGetSession() {
    const { data: { session } } = await supabaseClient.auth.getSession();
    return session;
}

async function supabaseGetUser() {
    const session = await supabaseGetSession();
    return session ? session.user : null;
}
