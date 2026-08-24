-- ============================================
-- VitalMatch - Supabase Database Schema
-- Run this in: Supabase Dashboard → SQL Editor
-- ============================================

-- Enable UUID generation
create extension if not exists "uuid-ossp";

-- ============================================
-- 1. PROFILES TABLE (extends auth.users)
-- ============================================
create table public.profiles (
  id uuid references auth.users(id) on delete cascade primary key,
  full_name text not null default '',
  email text default '',
  mobile text default '',
  blood_group text default '',
  state text default '',
  district text default '',
  city text default '',
  is_donor boolean default false,
  is_available boolean default true,
  last_donation_date date,
  notification_prefs jsonb default '{"emergency_alerts": true, "eligibility_reminders": true, "newsletter": false}'::jsonb,
  privacy_settings jsonb default '{"show_in_search": true, "share_history": true}'::jsonb,
  created_at timestamptz default now(),
  updated_at timestamptz default now()
);

-- ============================================
-- 2. EMERGENCY REQUESTS TABLE
-- ============================================
create table public.emergency_requests (
  id uuid default uuid_generate_v4() primary key,
  user_id uuid references public.profiles(id) on delete cascade not null,
  patient_name text not null,
  blood_group text not null,
  hospital_name text not null,
  contact_number text not null,
  location text not null,
  notes text default '',
  priority text default 'High',
  status text default 'Active',
  created_at timestamptz default now()
);

-- ============================================
-- 3. NOTIFICATIONS TABLE
-- ============================================
create table public.notifications (
  id uuid default uuid_generate_v4() primary key,
  user_id uuid references public.profiles(id) on delete cascade not null,
  title text not null,
  message text not null,
  type text default 'info',
  is_read boolean default false,
  link text default '',
  created_at timestamptz default now()
);

-- ============================================
-- 4. REPORTS TABLE
-- ============================================
create table public.reports (
  id uuid default uuid_generate_v4() primary key,
  reporter_id uuid references public.profiles(id) on delete cascade not null,
  report_type text not null,
  subject text not null,
  reason text not null,
  details text default '',
  status text default 'Under Review',
  target_emergency_id uuid references public.emergency_requests(id) on delete set null,
  target_donor_mobile text default '',
  created_at timestamptz default now()
);

-- ============================================
-- 5. DONATIONS TABLE
-- ============================================
create table public.donations (
  id uuid default uuid_generate_v4() primary key,
  donor_id uuid references public.profiles(id) on delete cascade not null,
  hospital text not null,
  units integer default 1,
  donation_date date not null,
  status text default 'Completed',
  created_at timestamptz default now()
);

-- ============================================
-- ENABLE ROW LEVEL SECURITY
-- ============================================
alter table public.profiles enable row level security;
alter table public.emergency_requests enable row level security;
alter table public.notifications enable row level security;
alter table public.reports enable row level security;
alter table public.donations enable row level security;

-- ============================================
-- RLS POLICIES - PROFILES
-- ============================================
create policy "Anyone can view profiles"
  on public.profiles for select using (true);

create policy "Users can insert own profile"
  on public.profiles for insert with check (auth.uid() = id);

create policy "Users can update own profile"
  on public.profiles for update using (auth.uid() = id);

-- ============================================
-- RLS POLICIES - EMERGENCY REQUESTS
-- ============================================
create policy "Anyone can view emergencies"
  on public.emergency_requests for select using (true);

create policy "Authenticated users can create emergencies"
  on public.emergency_requests for insert
  with check (auth.uid() = user_id);

create policy "Authenticated users can update emergencies"
  on public.emergency_requests for update
  using (auth.role() = 'authenticated');

-- ============================================
-- RLS POLICIES - NOTIFICATIONS
-- ============================================
create policy "Users can view own notifications"
  on public.notifications for select using (auth.uid() = user_id);

create policy "Authenticated can create notifications"
  on public.notifications for insert with check (true);

create policy "Users can update own notifications"
  on public.notifications for update using (auth.uid() = user_id);

create policy "Users can delete own notifications"
  on public.notifications for delete using (auth.uid() = user_id);

-- ============================================
-- RLS POLICIES - REPORTS
-- ============================================
create policy "Users can view own reports"
  on public.reports for select using (auth.uid() = reporter_id);

create policy "Users can create reports"
  on public.reports for insert with check (auth.uid() = reporter_id);

-- ============================================
-- RLS POLICIES - DONATIONS
-- ============================================
create policy "Users can view own donations"
  on public.donations for select using (auth.uid() = donor_id);

create policy "Users can create donations"
  on public.donations for insert with check (auth.uid() = donor_id);

-- ============================================
-- AUTO-CREATE PROFILE ON USER SIGNUP
-- ============================================
create or replace function public.handle_new_user()
returns trigger
language plpgsql
security definer set search_path = ''
as $$
begin
  insert into public.profiles (id, full_name, email, mobile)
  values (
    new.id,
    coalesce(new.raw_user_meta_data->>'full_name', ''),
    coalesce(new.email, ''),
    coalesce(new.raw_user_meta_data->>'mobile', '')
  );
  return new;
end;
$$;

create or replace trigger on_auth_user_created
  after insert on auth.users
  for each row execute function public.handle_new_user();

-- ============================================
-- INDEXES FOR PERFORMANCE
-- ============================================
create index idx_emergency_status on public.emergency_requests(status);
create index idx_emergency_blood on public.emergency_requests(blood_group);
create index idx_emergency_date on public.emergency_requests(created_at desc);
create index idx_notif_user on public.notifications(user_id);
create index idx_notif_read on public.notifications(is_read);
create index idx_profiles_blood on public.profiles(blood_group);
create index idx_profiles_donor on public.profiles(is_donor);
create index idx_reports_reporter on public.reports(reporter_id);
create index idx_donations_donor on public.donations(donor_id);
