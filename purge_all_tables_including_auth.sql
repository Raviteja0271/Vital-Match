-- =============================================================================
-- VitalMatch - Master Wipe Script (Public Database Tables + Auth Users)
-- Run in Supabase Dashboard -> SQL Editor
-- =============================================================================

-- 1. Truncate all public database tables
TRUNCATE TABLE 
    public.notifications,
    public.blood_requests,
    public.emergency_requests,
    public.donations,
    public.reports,
    public.profiles
CASCADE;

-- 2. Delete all authenticated user accounts from Supabase Auth system
-- (This prevents deleted users from logging in using old Auth credentials)
DELETE FROM auth.users;
