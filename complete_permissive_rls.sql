-- =============================================================================
-- VitalMatch - 100% Permissive Row Level Security (RLS) & Public Access Fix
-- Run this in Supabase Dashboard -> SQL Editor to eliminate all 401/403 errors!
-- =============================================================================

-- 1. Disable restrictive RLS across all tables (or grant full public access)
ALTER TABLE IF EXISTS public.profiles DISABLE ROW LEVEL SECURITY;
ALTER TABLE IF EXISTS public.emergency_requests DISABLE ROW LEVEL SECURITY;
ALTER TABLE IF EXISTS public.blood_requests DISABLE ROW LEVEL SECURITY;
ALTER TABLE IF EXISTS public.notifications DISABLE ROW LEVEL SECURITY;
ALTER TABLE IF EXISTS public.donations DISABLE ROW LEVEL SECURITY;
ALTER TABLE IF EXISTS public.reports DISABLE ROW LEVEL SECURITY;

-- 2. Grant full permissions to anon and authenticated roles
GRANT ALL ON ALL TABLES IN SCHEMA public TO anon, authenticated, postgres, service_role;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO anon, authenticated, postgres, service_role;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA public TO anon, authenticated, postgres, service_role;

-- 3. In case RLS is re-enabled, create 100% open policies for all operations:
DO $$
DECLARE
    tbl text;
BEGIN
    FOR tbl IN SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' LOOP
        EXECUTE format('ALTER TABLE public.%I ENABLE ROW LEVEL SECURITY;', tbl);
        EXECUTE format('DROP POLICY IF EXISTS "Allow All Select %I" ON public.%I;', tbl, tbl);
        EXECUTE format('DROP POLICY IF EXISTS "Allow All Insert %I" ON public.%I;', tbl, tbl);
        EXECUTE format('DROP POLICY IF EXISTS "Allow All Update %I" ON public.%I;', tbl, tbl);
        EXECUTE format('DROP POLICY IF EXISTS "Allow All Delete %I" ON public.%I;', tbl, tbl);
        
        EXECUTE format('CREATE POLICY "Allow All Select %I" ON public.%I FOR SELECT USING (true);', tbl, tbl);
        EXECUTE format('CREATE POLICY "Allow All Insert %I" ON public.%I FOR INSERT WITH CHECK (true);', tbl, tbl);
        EXECUTE format('CREATE POLICY "Allow All Update %I" ON public.%I FOR UPDATE USING (true);', tbl, tbl);
        EXECUTE format('CREATE POLICY "Allow All Delete %I" ON public.%I FOR DELETE USING (true);', tbl, tbl);
    END LOOP;
END $$;
