-- Execute this script in your Supabase SQL Editor to set up the database

-- Create donors table
CREATE TABLE IF NOT EXISTS public.donors (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    full_name TEXT NOT NULL,
    blood_group TEXT NOT NULL,
    phone_number TEXT NOT NULL,
    last_donation_date TEXT,
    is_available BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- Enable Row Level Security (RLS)
ALTER TABLE public.donors ENABLE ROW LEVEL SECURITY;

-- Drop existing policies to prevent "already exists" errors
DROP POLICY IF EXISTS "Allow authenticated users to insert donors" ON public.donors;
DROP POLICY IF EXISTS "Allow authenticated users to view donors" ON public.donors;

-- Create policies (For this simple app, we will allow all authenticated users to read and insert)
CREATE POLICY "Allow authenticated users to insert donors"
ON public.donors
FOR INSERT
TO authenticated
WITH CHECK (true);

CREATE POLICY "Allow authenticated users to view donors"
ON public.donors
FOR SELECT
TO authenticated
USING (true);

-- Adding location fields if they do not exist
ALTER TABLE public.donors ADD COLUMN IF NOT EXISTS country TEXT;
ALTER TABLE public.donors ADD COLUMN IF NOT EXISTS state TEXT;
ALTER TABLE public.donors ADD COLUMN IF NOT EXISTS district TEXT;
ALTER TABLE public.donors ADD COLUMN IF NOT EXISTS city TEXT;

-- You can further restrict this so a user can only update their own status

-- Create emergencies table
CREATE TABLE IF NOT EXISTS public.emergencies (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    blood_type TEXT NOT NULL,
    hospital_name TEXT NOT NULL,
    contact_number TEXT NOT NULL,
    message TEXT,
    status TEXT DEFAULT 'Active',
    user_name TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.emergencies ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Allow authenticated users to insert emergencies" ON public.emergencies;
DROP POLICY IF EXISTS "Allow authenticated users to view emergencies" ON public.emergencies;

CREATE POLICY "Allow authenticated users to insert emergencies"
ON public.emergencies
FOR INSERT
TO authenticated
WITH CHECK (true);

CREATE POLICY "Allow authenticated users to view emergencies"
ON public.emergencies
FOR SELECT
TO authenticated
USING (true);

-- Adding location fields to emergencies if they do not exist
ALTER TABLE public.emergencies ADD COLUMN IF NOT EXISTS country TEXT;
ALTER TABLE public.emergencies ADD COLUMN IF NOT EXISTS state TEXT;
ALTER TABLE public.emergencies ADD COLUMN IF NOT EXISTS district TEXT;
ALTER TABLE public.emergencies ADD COLUMN IF NOT EXISTS city TEXT;

-- Create notifications table
CREATE TABLE IF NOT EXISTS public.notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL,
    message TEXT NOT NULL,
    time TEXT NOT NULL,
    is_read BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- Enable RLS
ALTER TABLE public.notifications ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Allow authenticated users to insert notifications" ON public.notifications;
DROP POLICY IF EXISTS "Allow authenticated users to view notifications" ON public.notifications;

CREATE POLICY "Allow authenticated users to insert notifications"
ON public.notifications
FOR INSERT
TO authenticated
WITH CHECK (true);

CREATE POLICY "Allow authenticated users to view notifications"
ON public.notifications
FOR SELECT
TO authenticated
USING (true);

-- Create blood_requests table
CREATE TABLE IF NOT EXISTS public.blood_requests (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    donor_id UUID REFERENCES public.donors(id),
    donor_user_id UUID,
    requester_name TEXT NOT NULL,
    requester_phone TEXT NOT NULL,
    status TEXT DEFAULT 'Pending',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- Enable RLS
ALTER TABLE public.blood_requests ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Allow authenticated users to insert requests" ON public.blood_requests;
DROP POLICY IF EXISTS "Allow users to view their requests" ON public.blood_requests;
DROP POLICY IF EXISTS "Allow users to update their requests" ON public.blood_requests;

CREATE POLICY "Allow authenticated users to insert requests"
ON public.blood_requests
FOR INSERT
TO authenticated
WITH CHECK (true);

CREATE POLICY "Allow users to view their requests"
ON public.blood_requests
FOR SELECT
TO authenticated
USING (true);

CREATE POLICY "Allow users to update their requests"
ON public.blood_requests
FOR UPDATE
TO authenticated
USING (donor_user_id = auth.uid());

-- Add user_id to donors table
ALTER TABLE public.donors ADD COLUMN IF NOT EXISTS user_id UUID;
