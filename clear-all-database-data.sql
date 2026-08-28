-- =============================================================================
-- VitalMatch - SQL Query to Clear All Data in Supabase Database Tables
-- Run in Supabase Dashboard -> SQL Editor
-- =============================================================================

-- Step 1: Disable foreign key checks / truncate with cascade (Safest & Fastest)
TRUNCATE TABLE 
    public.notifications,
    public.blood_requests,
    public.emergency_requests,
    public.donations,
    public.reports,
    public.profiles
CASCADE;

-- Alternative Option (If TRUNCATE is restricted by permissions, use DELETE):
/*
DELETE FROM public.notifications;
DELETE FROM public.blood_requests;
DELETE FROM public.emergency_requests;
DELETE FROM public.donations;
DELETE FROM public.reports;
DELETE FROM public.profiles;
*/
