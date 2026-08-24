-- ============================================
-- VitalMatch - Clear ALL Database Data
-- Run this in Supabase SQL Editor
-- ============================================

-- Step 1: Delete all data from application tables (order matters due to foreign keys)
TRUNCATE TABLE notifications CASCADE;
TRUNCATE TABLE reports CASCADE;
TRUNCATE TABLE donations CASCADE;
TRUNCATE TABLE emergency_requests CASCADE;
TRUNCATE TABLE profiles CASCADE;

-- Step 2: Delete all registered users from Supabase Auth
-- This removes all user accounts completely
DELETE FROM auth.users;

-- Done! All data and user accounts have been cleared.
