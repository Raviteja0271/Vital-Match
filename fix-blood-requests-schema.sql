-- ============================================
-- VitalMatch - Fix Blood Requests Table & RLS
-- Run in Supabase Dashboard -> SQL Editor
-- ============================================

CREATE TABLE IF NOT EXISTS public.blood_requests (
  id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  donor_user_id uuid REFERENCES public.profiles(id) ON DELETE CASCADE,
  requester_name text NOT NULL,
  requester_phone text NOT NULL,
  status text DEFAULT 'Pending',
  created_at timestamptz DEFAULT now()
);

-- Enable Row Level Security
ALTER TABLE public.blood_requests ENABLE ROW LEVEL SECURITY;

-- Drop existing restrictive policies if any
DROP POLICY IF EXISTS "Anyone can select blood_requests" ON public.blood_requests;
DROP POLICY IF EXISTS "Anyone can insert blood_requests" ON public.blood_requests;
DROP POLICY IF EXISTS "Anyone can update blood_requests" ON public.blood_requests;
DROP POLICY IF EXISTS "Authenticated users can create blood_requests" ON public.blood_requests;
DROP POLICY IF EXISTS "Users can view own blood_requests" ON public.blood_requests;

-- Allow public & authenticated users to select, insert, and update blood_requests
CREATE POLICY "Anyone can select blood_requests" ON public.blood_requests FOR SELECT USING (true);
CREATE POLICY "Anyone can insert blood_requests" ON public.blood_requests FOR INSERT WITH CHECK (true);
CREATE POLICY "Anyone can update blood_requests" ON public.blood_requests FOR UPDATE USING (true);
