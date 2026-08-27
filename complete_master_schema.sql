-- ====================================================================
-- VitalMatch - Complete Master Database Schema & Migration Script
-- Production-Ready SQL Script for Supabase PostgreSQL
-- ====================================================================

-- 1. Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ====================================================================
-- 2. PROFILES TABLE (User profiles & donor registry)
-- ====================================================================
CREATE TABLE IF NOT EXISTS public.profiles (
  id uuid REFERENCES auth.users(id) ON DELETE CASCADE PRIMARY KEY,
  full_name text NOT NULL DEFAULT '',
  email text DEFAULT '',
  mobile text DEFAULT '',
  blood_group text DEFAULT '',
  country text DEFAULT 'India',
  state text DEFAULT '',
  district text DEFAULT '',
  city text DEFAULT '',
  is_donor boolean DEFAULT false,
  is_available boolean DEFAULT true,
  last_donation_date date,
  notification_prefs jsonb DEFAULT '{"emergency_alerts": true, "eligibility_reminders": true, "newsletter": false}'::jsonb,
  privacy_settings jsonb DEFAULT '{"show_in_search": true, "share_history": true}'::jsonb,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- Ensure missing columns exist if table was previously created
ALTER TABLE public.profiles ADD COLUMN IF NOT EXISTS country text DEFAULT 'India';
ALTER TABLE public.profiles ADD COLUMN IF NOT EXISTS state text DEFAULT '';
ALTER TABLE public.profiles ADD COLUMN IF NOT EXISTS district text DEFAULT '';
ALTER TABLE public.profiles ADD COLUMN IF NOT EXISTS city text DEFAULT '';
ALTER TABLE public.profiles ADD COLUMN IF NOT EXISTS is_donor boolean DEFAULT false;
ALTER TABLE public.profiles ADD COLUMN IF NOT EXISTS is_available boolean DEFAULT true;
ALTER TABLE public.profiles ADD COLUMN IF NOT EXISTS last_donation_date date;

-- ====================================================================
-- 3. EMERGENCY REQUESTS TABLE (Receptor emergency postings)
-- ====================================================================
CREATE TABLE IF NOT EXISTS public.emergency_requests (
  id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  user_id uuid REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
  patient_name text NOT NULL,
  blood_group text NOT NULL,
  hospital_name text NOT NULL,
  contact_number text NOT NULL,
  location text NOT NULL,
  country text DEFAULT 'India',
  state text DEFAULT '',
  district text DEFAULT '',
  city text DEFAULT '',
  notes text DEFAULT '',
  priority text DEFAULT 'High',
  status text DEFAULT 'Active',
  created_at timestamptz DEFAULT now()
);

-- Ensure optional columns exist
ALTER TABLE public.emergency_requests ADD COLUMN IF NOT EXISTS country text DEFAULT 'India';
ALTER TABLE public.emergency_requests ADD COLUMN IF NOT EXISTS state text DEFAULT '';
ALTER TABLE public.emergency_requests ADD COLUMN IF NOT EXISTS district text DEFAULT '';
ALTER TABLE public.emergency_requests ADD COLUMN IF NOT EXISTS city text DEFAULT '';

-- ====================================================================
-- 4. BLOOD REQUESTS TABLE (Direct donor requests)
-- ====================================================================
CREATE TABLE IF NOT EXISTS public.blood_requests (
  id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  donor_user_id uuid REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
  requester_name text NOT NULL,
  requester_phone text NOT NULL,
  status text DEFAULT 'Pending',
  created_at timestamptz DEFAULT now()
);

-- ====================================================================
-- 5. NOTIFICATIONS TABLE (User alerts & notifications)
-- ====================================================================
CREATE TABLE IF NOT EXISTS public.notifications (
  id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  user_id uuid REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
  title text NOT NULL,
  message text NOT NULL,
  type text DEFAULT 'info',
  is_read boolean DEFAULT false,
  link text DEFAULT '',
  created_at timestamptz DEFAULT now()
);

-- ====================================================================
-- 6. DONATIONS TABLE (Donor history records)
-- ====================================================================
CREATE TABLE IF NOT EXISTS public.donations (
  id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  donor_id uuid REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
  hospital text NOT NULL,
  units integer DEFAULT 1,
  donation_date date NOT NULL,
  status text DEFAULT 'Completed',
  created_at timestamptz DEFAULT now()
);

-- ====================================================================
-- 7. REPORTS TABLE (Content moderation & user reporting)
-- ====================================================================
CREATE TABLE IF NOT EXISTS public.reports (
  id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  reporter_id uuid REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
  report_type text NOT NULL,
  subject text NOT NULL,
  reason text NOT NULL,
  details text DEFAULT '',
  status text DEFAULT 'Under Review',
  target_emergency_id uuid REFERENCES public.emergency_requests(id) ON DELETE SET NULL,
  target_donor_mobile text DEFAULT '',
  created_at timestamptz DEFAULT now()
);

-- ====================================================================
-- 8. ROW LEVEL SECURITY (RLS) POLICIES
-- ====================================================================

-- Enable RLS on all tables
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.emergency_requests ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.blood_requests ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.notifications ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.donations ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.reports ENABLE ROW LEVEL SECURITY;

-- Drop existing policies to prevent duplication errors
DROP POLICY IF EXISTS "Public profile access" ON public.profiles;
DROP POLICY IF EXISTS "Public emergency access" ON public.emergency_requests;
DROP POLICY IF EXISTS "Public blood_requests access" ON public.blood_requests;
DROP POLICY IF EXISTS "Public notifications access" ON public.notifications;
DROP POLICY IF EXISTS "Public donations access" ON public.donations;
DROP POLICY IF EXISTS "Public reports access" ON public.reports;

-- Create unified, permissive access policies for development/production
CREATE POLICY "Public profile access" ON public.profiles FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Public emergency access" ON public.emergency_requests FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Public blood_requests access" ON public.blood_requests FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Public notifications access" ON public.notifications FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Public donations access" ON public.donations FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Public reports access" ON public.reports FOR ALL USING (true) WITH CHECK (true);

-- ====================================================================
-- 9. PERFORMANCE INDEXES
-- ====================================================================
CREATE INDEX IF NOT EXISTS idx_profiles_blood ON public.profiles(blood_group);
CREATE INDEX IF NOT EXISTS idx_profiles_district ON public.profiles(district);
CREATE INDEX IF NOT EXISTS idx_profiles_donor ON public.profiles(is_donor);
CREATE INDEX IF NOT EXISTS idx_emergency_status ON public.emergency_requests(status);
CREATE INDEX IF NOT EXISTS idx_emergency_blood ON public.emergency_requests(blood_group);
CREATE INDEX IF NOT EXISTS idx_blood_requests_donor ON public.blood_requests(donor_user_id);
CREATE INDEX IF NOT EXISTS idx_notifications_user ON public.notifications(user_id);

-- ====================================================================
-- 10. AUTO-PROFILE TRIGGER FOR NEW USERS
-- ====================================================================
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS trigger
LANGUAGE plpgsql
SECURITY DEFINER SET search_path = ''
AS $$
BEGIN
  INSERT INTO public.profiles (id, full_name, email, mobile)
  VALUES (
    new.id,
    COALESCE(new.raw_user_meta_data->>'full_name', ''),
    COALESCE(new.email, ''),
    COALESCE(new.raw_user_meta_data->>'mobile', '')
  )
  ON CONFLICT (id) DO NOTHING;
  RETURN new;
END;
$$;

DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;
CREATE TRIGGER on_auth_user_created
  AFTER INSERT ON auth.users
  FOR EACH ROW EXECUTE FUNCTION public.handle_new_user();

-- Done! Complete database schema is ready.
