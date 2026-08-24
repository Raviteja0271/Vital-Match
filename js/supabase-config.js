// ============================================
// VitalMatch - Supabase Configuration
// ============================================
// HOW TO GET YOUR ANON KEY:
// 1. Go to https://supabase.com/dashboard
// 2. Open your project (wcoipyffkhhvcpknrqpb)
// 3. Go to Settings → API → Project API Keys
// 4. Copy the "anon public" key (starts with "eyJ...")
// 5. Paste it below replacing the placeholder
// ============================================

const SUPABASE_URL = 'https://wcoipyffkhhvcpknrqpb.supabase.co';
const SUPABASE_ANON_KEY = 'sb_publishable_dE6gJYL2yGgxS9lIEKoqHA_FDooPDHg';

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
