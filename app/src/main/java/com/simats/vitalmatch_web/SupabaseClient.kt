package com.simats.vitalmatch_web

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    private const val SUPABASE_URL = "https://wcoipyffkhhvcpknrqpb.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_dE6gJYL2yGgxS9lIEKoqHA_FDooPDHg"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }
}
