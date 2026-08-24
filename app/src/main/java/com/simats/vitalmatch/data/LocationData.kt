package com.simats.vitalmatch.data

object LocationData {
    val countries = listOf("India")

    val statesMap = mapOf(
        "India" to listOf(
            "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", 
            "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", 
            "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", 
            "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", 
            "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal",
            "Andaman and Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu",
            "Delhi", "Jammu and Kashmir", "Ladakh", "Lakshadweep", "Puducherry"
        )
    )

    val districtsMap = mapOf(
        // ========== STATES ==========
        "Andhra Pradesh" to listOf(
            "Anantapur", "Chittoor", "East Godavari", "Guntur", "Krishna",
            "Kurnool", "Nellore", "Prakasam", "Srikakulam", "Visakhapatnam",
            "Vizianagaram", "West Godavari", "YSR Kadapa"
        ),
        "Arunachal Pradesh" to listOf(
            "Itanagar Capital Complex", "Tawang", "West Kameng", "Papum Pare", "Lower Subansiri"
        ),
        "Assam" to listOf(
            "Baksa", "Cachar", "Dibrugarh", "Kamrup Metropolitan", "Kamrup",
            "Jorhat", "Nagaon", "Sonitpur", "Tinsukia"
        ),
        "Bihar" to listOf(
            "Araria", "Bhagalpur", "Gaya", "Muzaffarpur", "Nalanda",
            "Patna", "Purnia", "Saran", "Vaishali"
        ),
        "Chhattisgarh" to listOf(
            "Bastar", "Bilaspur", "Durg", "Korba", "Raigarh", "Raipur", "Rajnandgaon"
        ),
        "Goa" to listOf(
            "North Goa", "South Goa"
        ),
        "Gujarat" to listOf(
            "Ahmedabad", "Amreli", "Anand", "Bhavnagar", "Gandhinagar",
            "Jamnagar", "Junagadh", "Kutch", "Rajkot", "Surat", "Vadodara"
        ),
        "Haryana" to listOf(
            "Ambala", "Faridabad", "Gurugram", "Hisar", "Karnal",
            "Kurukshetra", "Panipat", "Rohtak", "Sonipat"
        ),
        "Himachal Pradesh" to listOf(
            "Bilaspur", "Kangra", "Kullu", "Mandi", "Shimla", "Solan"
        ),
        "Jharkhand" to listOf(
            "Bokaro", "Dhanbad", "East Singhbhum", "Hazaribagh", "Ranchi", "West Singhbhum"
        ),
        "Karnataka" to listOf(
            "Bagalkot", "Bangalore Rural", "Bangalore Urban", "Belgaum", "Bellary",
            "Dakshina Kannada", "Dharwad", "Gulbarga", "Hassan", "Mysuru",
            "Raichur", "Shimoga", "Tumkur", "Udupi"
        ),
        "Kerala" to listOf(
            "Alappuzha", "Ernakulam", "Idukki", "Kannur", "Kasaragod",
            "Kollam", "Kottayam", "Kozhikode", "Malappuram", "Palakkad",
            "Pathanamthitta", "Thiruvananthapuram", "Thrissur", "Wayanad"
        ),
        "Madhya Pradesh" to listOf(
            "Bhopal", "Gwalior", "Indore", "Jabalpur", "Rewa", "Sagar", "Satna", "Ujjain"
        ),
        "Maharashtra" to listOf(
            "Ahmednagar", "Aurangabad", "Kolhapur", "Mumbai City", "Mumbai Suburban",
            "Nagpur", "Nashik", "Pune", "Raigad", "Sangli", "Satara", "Solapur", "Thane"
        ),
        "Manipur" to listOf(
            "Bishnupur", "Imphal East", "Imphal West", "Thoubal"
        ),
        "Meghalaya" to listOf(
            "East Garo Hills", "East Khasi Hills", "West Garo Hills", "Ri-Bhoi"
        ),
        "Mizoram" to listOf(
            "Aizawl", "Lunglei", "Champhai"
        ),
        "Nagaland" to listOf(
            "Dimapur", "Kohima", "Mokokchung"
        ),
        "Odisha" to listOf(
            "Balasore", "Cuttack", "Ganjam", "Khordha", "Koraput",
            "Mayurbhanj", "Puri", "Sambalpur", "Sundargarh"
        ),
        "Punjab" to listOf(
            "Amritsar", "Bathinda", "Jalandhar", "Ludhiana", "Mohali", "Patiala", "Sangrur"
        ),
        "Rajasthan" to listOf(
            "Ajmer", "Alwar", "Bikaner", "Jaipur", "Jodhpur", "Kota", "Sikar", "Udaipur"
        ),
        "Sikkim" to listOf(
            "East Sikkim", "North Sikkim", "South Sikkim", "West Sikkim"
        ),
        "Tamil Nadu" to listOf(
            "Chennai", "Coimbatore", "Cuddalore", "Erode", "Kancheepuram",
            "Kanyakumari", "Madurai", "Nagapattinam", "Salem", "Thanjavur",
            "Tiruchirappalli", "Tirunelveli", "Tiruppur", "Tiruvallur",
            "Tiruvannamalai", "Vellore", "Villupuram"
        ),
        "Telangana" to listOf(
            "Adilabad", "Hyderabad", "Karimnagar", "Khammam", "Mahabubnagar",
            "Medak", "Nalgonda", "Nizamabad", "Rangareddy", "Warangal"
        ),
        "Tripura" to listOf(
            "Dhalai", "North Tripura", "South Tripura", "West Tripura"
        ),
        "Uttar Pradesh" to listOf(
            "Agra", "Aligarh", "Allahabad", "Bareilly", "Ghaziabad",
            "Gorakhpur", "Jhansi", "Kanpur Nagar", "Lucknow", "Mathura",
            "Meerut", "Moradabad", "Noida", "Varanasi"
        ),
        "Uttarakhand" to listOf(
            "Chamoli", "Dehradun", "Haridwar", "Nainital", "Udham Singh Nagar"
        ),
        "West Bengal" to listOf(
            "Bankura", "Bardhaman", "Darjeeling", "Hooghly", "Howrah",
            "Kolkata", "Malda", "Murshidabad", "Nadia", "North 24 Parganas", "South 24 Parganas"
        ),

        // ========== UNION TERRITORIES ==========
        "Andaman and Nicobar Islands" to listOf(
            "Nicobar", "North and Middle Andaman", "South Andaman"
        ),
        "Chandigarh" to listOf(
            "Chandigarh"
        ),
        "Dadra and Nagar Haveli and Daman and Diu" to listOf(
            "Dadra and Nagar Haveli", "Daman", "Diu"
        ),
        "Delhi" to listOf(
            "Central Delhi", "East Delhi", "New Delhi", "North Delhi",
            "North West Delhi", "South Delhi", "South West Delhi", "West Delhi"
        ),
        "Jammu and Kashmir" to listOf(
            "Anantnag", "Baramulla", "Jammu", "Kathua", "Pulwama", "Srinagar", "Udhampur"
        ),
        "Ladakh" to listOf(
            "Kargil", "Leh"
        ),
        "Lakshadweep" to listOf(
            "Lakshadweep"
        ),
        "Puducherry" to listOf(
            "Karaikal", "Mahe", "Puducherry", "Yanam"
        )
    )

    val citiesMap = mapOf(
        // ========== ANDHRA PRADESH ==========
        "Anantapur" to listOf("Anantapur", "Hindupur", "Guntakal", "Dharmavaram", "Tadpatri"),
        "Chittoor" to listOf("Tirupati", "Chittoor", "Madanapalle", "Srikalahasti", "Punganur"),
        "East Godavari" to listOf("Kakinada", "Rajahmundry", "Samalkot", "Amalapuram", "Peddapuram"),
        "Guntur" to listOf("Guntur", "Tenali", "Narasaraopet", "Mangalagiri", "Bapatla"),
        "Krishna" to listOf("Vijayawada", "Machilipatnam", "Gudivada", "Nuzvid", "Jaggaiahpet"),
        "Kurnool" to listOf("Kurnool", "Nandyal", "Adoni", "Yemmiganur", "Atmakur"),
        "Nellore" to listOf("Nellore", "Kavali", "Gudur", "Sullurpeta", "Atmakur"),
        "Prakasam" to listOf("Ongole", "Chirala", "Markapur", "Kandukur", "Darsi"),
        "Srikakulam" to listOf("Srikakulam", "Narasannapeta", "Amadalavalasa", "Palasa"),
        "Visakhapatnam" to listOf("Visakhapatnam", "Gajuwaka", "Anakapalle", "Bheemunipatnam", "Narsipatnam"),
        "Vizianagaram" to listOf("Vizianagaram", "Bobbili", "Rajam", "Parvathipuram"),
        "West Godavari" to listOf("Eluru", "Bhimavaram", "Tadepalligudem", "Tanuku", "Narsapuram"),
        "YSR Kadapa" to listOf("Kadapa", "Proddatur", "Rajampet", "Mydukur", "Jammalamadugu"),

        // ========== ARUNACHAL PRADESH ==========
        "Itanagar Capital Complex" to listOf("Itanagar", "Naharlagun", "Banderdewa"),
        "Tawang" to listOf("Tawang", "Lumla", "Jang"),
        "West Kameng" to listOf("Bomdila", "Dirang", "Kalaktang"),
        "Papum Pare" to listOf("Yupia", "Doimukh", "Balijan"),
        "Lower Subansiri" to listOf("Ziro", "Raga", "Old Ziro"),

        // ========== ASSAM ==========
        "Baksa" to listOf("Mushalpur", "Tamulpur", "Barama"),
        "Cachar" to listOf("Silchar", "Lakhipur", "Sonai"),
        "Dibrugarh" to listOf("Dibrugarh", "Naharkatia", "Chabua", "Barbaruah"),
        "Kamrup Metropolitan" to listOf("Guwahati", "Dispur", "North Guwahati", "Paltan Bazaar"),
        "Kamrup" to listOf("Amingaon", "Rangia", "Boko", "Hajo"),
        "Jorhat" to listOf("Jorhat", "Titabor", "Mariani"),
        "Nagaon" to listOf("Nagaon", "Hojai", "Lanka", "Dhing"),
        "Sonitpur" to listOf("Tezpur", "Rangapara", "Dhekiajuli"),
        "Tinsukia" to listOf("Tinsukia", "Digboi", "Margherita", "Doomdooma"),

        // ========== BIHAR ==========
        "Araria" to listOf("Araria", "Forbesganj", "Raniganj"),
        "Bhagalpur" to listOf("Bhagalpur", "Sultanganj", "Naugachia"),
        "Gaya" to listOf("Gaya", "Bodhgaya", "Sherghati", "Tekari"),
        "Muzaffarpur" to listOf("Muzaffarpur", "Motipur", "Sitamarhi"),
        "Nalanda" to listOf("Bihar Sharif", "Rajgir", "Hilsa"),
        "Patna" to listOf("Patna", "Danapur", "Phulwari", "Masaurhi", "Barh"),
        "Purnia" to listOf("Purnia", "Banmankhi", "Kasba"),
        "Saran" to listOf("Chhapra", "Marhaura", "Revelganj"),
        "Vaishali" to listOf("Hajipur", "Mahua", "Lalganj"),

        // ========== CHHATTISGARH ==========
        "Bastar" to listOf("Jagdalpur", "Lohandiguda", "Bakawand"),
        "Bilaspur" to listOf("Bilaspur", "Ratanpur", "Takhatpur", "Lormi"),
        "Durg" to listOf("Durg", "Bhilai", "Patan", "Dhamdha"),
        "Korba" to listOf("Korba", "Katghora", "Pali"),
        "Raigarh" to listOf("Raigarh", "Sarangarh", "Gharghoda"),
        "Raipur" to listOf("Raipur", "Abhanpur", "Arang", "Tilda"),
        "Rajnandgaon" to listOf("Rajnandgaon", "Dongargarh", "Khairagarh"),

        // ========== GOA ==========
        "North Goa" to listOf("Panaji", "Mapusa", "Bicholim", "Valpoi", "Ponda"),
        "South Goa" to listOf("Margao", "Vasco da Gama", "Cuncolim", "Quepem", "Canacona"),

        // ========== GUJARAT ==========
        "Ahmedabad" to listOf("Ahmedabad", "Dhandhuka", "Bavla", "Sanand", "Viramgam"),
        "Amreli" to listOf("Amreli", "Rajula", "Savarkundla"),
        "Anand" to listOf("Anand", "Borsad", "Petlad", "Khambhat"),
        "Bhavnagar" to listOf("Bhavnagar", "Palitana", "Sihor", "Mahuva"),
        "Gandhinagar" to listOf("Gandhinagar", "Kalol", "Dehgam", "Mansa"),
        "Jamnagar" to listOf("Jamnagar", "Dhrol", "Jodia", "Kalavad"),
        "Junagadh" to listOf("Junagadh", "Veraval", "Manavadar", "Vanthali"),
        "Kutch" to listOf("Bhuj", "Gandhidham", "Anjar", "Mundra", "Mandvi"),
        "Rajkot" to listOf("Rajkot", "Gondal", "Jetpur", "Morbi"),
        "Surat" to listOf("Surat", "Bardoli", "Mandvi", "Olpad", "Kamrej"),
        "Vadodara" to listOf("Vadodara", "Dabhoi", "Padra", "Savli"),

        // ========== HARYANA ==========
        "Ambala" to listOf("Ambala", "Barara", "Naraingarh"),
        "Faridabad" to listOf("Faridabad", "Ballabgarh", "Palwal"),
        "Gurugram" to listOf("Gurugram", "Sohna", "Pataudi", "Manesar"),
        "Hisar" to listOf("Hisar", "Hansi", "Barwala"),
        "Karnal" to listOf("Karnal", "Gharaunda", "Nilokheri", "Indri"),
        "Kurukshetra" to listOf("Kurukshetra", "Thanesar", "Pehowa", "Ladwa"),
        "Panipat" to listOf("Panipat", "Samalkha", "Israna"),
        "Rohtak" to listOf("Rohtak", "Kalanaur", "Meham"),
        "Sonipat" to listOf("Sonipat", "Ganaur", "Gohana"),

        // ========== HIMACHAL PRADESH ==========
        "Kangra" to listOf("Dharamshala", "Kangra", "Palampur", "Baijnath"),
        "Kullu" to listOf("Kullu", "Manali", "Bhuntar", "Banjar"),
        "Mandi" to listOf("Mandi", "Sundernagar", "Jogindernagar"),
        "Shimla" to listOf("Shimla", "Theog", "Rampur Bushahr", "Kufri"),
        "Solan" to listOf("Solan", "Nalagarh", "Baddi", "Parwanoo"),

        // ========== JHARKHAND ==========
        "Bokaro" to listOf("Bokaro Steel City", "Chas", "Gomia"),
        "Dhanbad" to listOf("Dhanbad", "Jharia", "Sindri", "Katras"),
        "East Singhbhum" to listOf("Jamshedpur", "Gamharia", "Jugsalai", "Mango"),
        "Hazaribagh" to listOf("Hazaribagh", "Barhi", "Ichak"),
        "Ranchi" to listOf("Ranchi", "Namkum", "Kanke", "Ratu"),
        "West Singhbhum" to listOf("Chaibasa", "Chakradharpur", "Jagannathpur"),

        // ========== KARNATAKA ==========
        "Bagalkot" to listOf("Bagalkot", "Badami", "Jamkhandi", "Mudhol"),
        "Bangalore Rural" to listOf("Devanahalli", "Doddaballapur", "Nelamangala", "Hoskote"),
        "Bangalore Urban" to listOf("Bengaluru", "Yelahanka", "Anekal", "Whitefield", "Electronic City"),
        "Belgaum" to listOf("Belgaum", "Gokak", "Athani", "Chikkodi", "Raibag"),
        "Bellary" to listOf("Bellary", "Hospet", "Sandur", "Siruguppa"),
        "Dakshina Kannada" to listOf("Mangaluru", "Puttur", "Bantwal", "Sullia", "Belthangady"),
        "Dharwad" to listOf("Dharwad", "Hubli", "Kalghatgi", "Kundgol"),
        "Gulbarga" to listOf("Kalaburagi", "Sedam", "Aland", "Chincholi"),
        "Hassan" to listOf("Hassan", "Arsikere", "Channarayapatna", "Belur"),
        "Mysuru" to listOf("Mysuru", "Nanjangud", "T. Narasipura", "Hunsur", "K.R. Nagar"),
        "Raichur" to listOf("Raichur", "Manvi", "Sindhanur", "Devadurga"),
        "Shimoga" to listOf("Shimoga", "Bhadravathi", "Sagar", "Thirthahalli"),
        "Tumkur" to listOf("Tumkur", "Tiptur", "Sira", "Madhugiri"),
        "Udupi" to listOf("Udupi", "Kundapura", "Karkala", "Brahmavara"),

        // ========== KERALA ==========
        "Alappuzha" to listOf("Alappuzha", "Cherthala", "Kayamkulam", "Haripad"),
        "Ernakulam" to listOf("Kochi", "Aluva", "Angamaly", "Perumbavoor", "Muvattupuzha"),
        "Idukki" to listOf("Painavu", "Thodupuzha", "Munnar", "Adimali"),
        "Kannur" to listOf("Kannur", "Thalassery", "Payyanur", "Mattannur"),
        "Kasaragod" to listOf("Kasaragod", "Kanhangad", "Nileshwar"),
        "Kollam" to listOf("Kollam", "Karunagappally", "Punalur", "Kottarakkara"),
        "Kottayam" to listOf("Kottayam", "Pala", "Changanassery", "Vaikom"),
        "Kozhikode" to listOf("Kozhikode", "Vadakara", "Koyilandy", "Feroke"),
        "Malappuram" to listOf("Malappuram", "Manjeri", "Perinthalmanna", "Tirur", "Ponnani"),
        "Palakkad" to listOf("Palakkad", "Ottapalam", "Shoranur", "Chittur", "Mannarkkad"),
        "Pathanamthitta" to listOf("Pathanamthitta", "Adoor", "Thiruvalla", "Ranni"),
        "Thiruvananthapuram" to listOf("Thiruvananthapuram", "Neyyattinkara", "Attingal", "Nedumangad"),
        "Thrissur" to listOf("Thrissur", "Guruvayur", "Chalakudy", "Kunnamkulam", "Irinjalakuda"),
        "Wayanad" to listOf("Kalpetta", "Sulthan Bathery", "Mananthavady"),

        // ========== MADHYA PRADESH ==========
        "Bhopal" to listOf("Bhopal", "Berasia", "Sehore"),
        "Gwalior" to listOf("Gwalior", "Dabra", "Bhitarwar"),
        "Indore" to listOf("Indore", "Mhow", "Sanwer", "Depalpur"),
        "Jabalpur" to listOf("Jabalpur", "Sihora", "Patan"),
        "Rewa" to listOf("Rewa", "Mauganj", "Hanumana"),
        "Sagar" to listOf("Sagar", "Khurai", "Bina", "Banda"),
        "Satna" to listOf("Satna", "Maihar", "Amarpatan"),
        "Ujjain" to listOf("Ujjain", "Nagda", "Mahidpur", "Tarana"),

        // ========== MAHARASHTRA ==========
        "Ahmednagar" to listOf("Ahmednagar", "Shrirampur", "Rahuri", "Sangamner"),
        "Aurangabad" to listOf("Aurangabad", "Paithan", "Vaijapur", "Kannad"),
        "Kolhapur" to listOf("Kolhapur", "Ichalkaranji", "Jaysingpur", "Gadhinglaj"),
        "Mumbai City" to listOf("Mumbai", "Colaba", "Dadar", "Byculla", "Worli"),
        "Mumbai Suburban" to listOf("Andheri", "Borivali", "Kandivali", "Malad", "Goregaon"),
        "Nagpur" to listOf("Nagpur", "Kamptee", "Hingna", "Narkhed"),
        "Nashik" to listOf("Nashik", "Malegaon", "Igatpuri", "Sinnar", "Manmad"),
        "Pune" to listOf("Pune", "Pimpri-Chinchwad", "Baramati", "Junnar", "Shirur"),
        "Raigad" to listOf("Alibag", "Panvel", "Pen", "Khalapur", "Karjat"),
        "Sangli" to listOf("Sangli", "Miraj", "Tasgaon", "Vita"),
        "Satara" to listOf("Satara", "Karad", "Wai", "Mahabaleshwar"),
        "Solapur" to listOf("Solapur", "Pandharpur", "Barshi", "Akkalkot"),
        "Thane" to listOf("Thane", "Kalyan", "Dombivli", "Ulhasnagar", "Bhiwandi"),

        // ========== MANIPUR ==========
        "Bishnupur" to listOf("Bishnupur", "Moirang", "Ningthoukhong"),
        "Imphal East" to listOf("Porompat", "Jiribam", "Lamlai"),
        "Imphal West" to listOf("Imphal", "Lamphel", "Singjamei"),
        "Thoubal" to listOf("Thoubal", "Kakching", "Lilong"),

        // ========== MEGHALAYA ==========
        "East Garo Hills" to listOf("Williamnagar", "Rongjeng", "Songsak"),
        "East Khasi Hills" to listOf("Shillong", "Nongpoh", "Cherrapunji", "Mawkyrwat"),
        "West Garo Hills" to listOf("Tura", "Dadenggre", "Selsella"),
        "Ri-Bhoi" to listOf("Nongpoh", "Umroi", "Byrnihat"),

        // ========== MIZORAM ==========
        "Aizawl" to listOf("Aizawl", "Darlawn", "Saitual"),
        "Lunglei" to listOf("Lunglei", "Hnahthial", "Lawngtlai"),
        "Champhai" to listOf("Champhai", "Khawzawl", "Biate"),

        // ========== NAGALAND ==========
        "Dimapur" to listOf("Dimapur", "Chumukedima", "Medziphema"),
        "Kohima" to listOf("Kohima", "Tseminyu", "Chiephobozou"),
        "Mokokchung" to listOf("Mokokchung", "Tuli", "Changtongya"),

        // ========== ODISHA ==========
        "Balasore" to listOf("Balasore", "Jaleswar", "Soro", "Nilgiri"),
        "Cuttack" to listOf("Cuttack", "Choudwar", "Banki", "Athagarh"),
        "Ganjam" to listOf("Berhampur", "Chatrapur", "Aska", "Hinjilicut"),
        "Khordha" to listOf("Bhubaneswar", "Jatni", "Balugaon", "Khordha"),
        "Koraput" to listOf("Koraput", "Jeypore", "Sunabeda"),
        "Mayurbhanj" to listOf("Baripada", "Rairangpur", "Karanjia"),
        "Puri" to listOf("Puri", "Konark", "Pipili", "Nimapara"),
        "Sambalpur" to listOf("Sambalpur", "Burla", "Hirakud", "Kuchinda"),
        "Sundargarh" to listOf("Rourkela", "Sundargarh", "Rajgangpur", "Bonai"),

        // ========== PUNJAB ==========
        "Amritsar" to listOf("Amritsar", "Ajnala", "Baba Bakala", "Tarn Taran"),
        "Bathinda" to listOf("Bathinda", "Rampura Phul", "Maur", "Talwandi Sabo"),
        "Jalandhar" to listOf("Jalandhar", "Phagwara", "Nakodar", "Phillaur", "Kartarpur"),
        "Ludhiana" to listOf("Ludhiana", "Khanna", "Jagraon", "Samrala", "Raikot"),
        "Mohali" to listOf("Mohali", "Kharar", "Derabassi", "Zirakpur"),
        "Patiala" to listOf("Patiala", "Rajpura", "Samana", "Nabha"),
        "Sangrur" to listOf("Sangrur", "Malerkotla", "Barnala", "Dhuri"),

        // ========== RAJASTHAN ==========
        "Ajmer" to listOf("Ajmer", "Beawar", "Kishangarh", "Nasirabad"),
        "Alwar" to listOf("Alwar", "Bhiwadi", "Behror", "Rajgarh"),
        "Bikaner" to listOf("Bikaner", "Nokha", "Lunkaransar"),
        "Jaipur" to listOf("Jaipur", "Sanganer", "Amber", "Chomu", "Chaksu"),
        "Jodhpur" to listOf("Jodhpur", "Bilara", "Pipar City", "Phalodi"),
        "Kota" to listOf("Kota", "Ramganj Mandi", "Sangod"),
        "Sikar" to listOf("Sikar", "Fatehpur", "Lachhmangarh", "Neem Ka Thana"),
        "Udaipur" to listOf("Udaipur", "Rajsamand", "Nathdwara", "Salumber"),

        // ========== SIKKIM ==========
        "East Sikkim" to listOf("Gangtok", "Singtam", "Rangpo", "Pakyong"),
        "North Sikkim" to listOf("Mangan", "Chungthang", "Lachung"),
        "South Sikkim" to listOf("Namchi", "Jorethang", "Ravangla"),
        "West Sikkim" to listOf("Gyalshing", "Pelling", "Soreng"),

        // ========== TAMIL NADU ==========
        "Chennai" to listOf("Chennai", "Tambaram", "Avadi", "Ambattur", "Chromepet"),
        "Coimbatore" to listOf("Coimbatore", "Pollachi", "Mettupalayam", "Sulur", "Valparai"),
        "Cuddalore" to listOf("Cuddalore", "Chidambaram", "Virudhachalam", "Neyveli"),
        "Erode" to listOf("Erode", "Bhavani", "Gobichettipalayam", "Sathyamangalam"),
        "Kancheepuram" to listOf("Kancheepuram", "Sriperumbudur", "Uthiramerur", "Walajabad"),
        "Kanyakumari" to listOf("Nagercoil", "Marthandam", "Colachel", "Padmanabhapuram"),
        "Madurai" to listOf("Madurai", "Melur", "Usilampatti", "Vadipatti", "Thirumangalam"),
        "Nagapattinam" to listOf("Nagapattinam", "Sirkazhi", "Mayiladuthurai", "Tharangambadi"),
        "Salem" to listOf("Salem", "Mettur", "Attur", "Omalur"),
        "Thanjavur" to listOf("Thanjavur", "Kumbakonam", "Pattukkottai", "Orathanadu"),
        "Tiruchirappalli" to listOf("Tiruchirappalli", "Srirangam", "Lalgudi", "Musiri", "Thuraiyur"),
        "Tirunelveli" to listOf("Tirunelveli", "Palayamkottai", "Ambasamudram", "Tenkasi"),
        "Tiruppur" to listOf("Tiruppur", "Avinashi", "Udumalpet", "Dharapuram", "Kangeyam"),
        "Tiruvallur" to listOf("Tiruvallur", "Tiruttani", "Ponneri", "Gummidipoondi"),
        "Tiruvannamalai" to listOf("Tiruvannamalai", "Arani", "Polur", "Chengam"),
        "Vellore" to listOf("Vellore", "Ambur", "Gudiyatham", "Vaniyambadi", "Ranipet"),
        "Villupuram" to listOf("Villupuram", "Tindivanam", "Gingee", "Kallakurichi"),

        // ========== TELANGANA ==========
        "Adilabad" to listOf("Adilabad", "Nirmal", "Mancherial", "Bellampally"),
        "Hyderabad" to listOf("Hyderabad", "Secunderabad", "Charminar", "Begumpet"),
        "Karimnagar" to listOf("Karimnagar", "Jagtial", "Peddapalli", "Korutla"),
        "Khammam" to listOf("Khammam", "Kothagudem", "Yellandu", "Sathupalli"),
        "Mahabubnagar" to listOf("Mahabubnagar", "Jadcherla", "Narayanpet", "Wanaparthy"),
        "Medak" to listOf("Sangareddy", "Medak", "Siddipet", "Zahirabad"),
        "Nalgonda" to listOf("Nalgonda", "Suryapet", "Miryalaguda", "Devarakonda"),
        "Nizamabad" to listOf("Nizamabad", "Kamareddy", "Bodhan", "Armoor"),
        "Rangareddy" to listOf("Shamshabad", "Rajendra Nagar", "Chevella", "Ibrahimpatnam"),
        "Warangal" to listOf("Warangal", "Hanamkonda", "Kazipet", "Jangaon"),

        // ========== TRIPURA ==========
        "Dhalai" to listOf("Ambassa", "Kamalpur", "Chawmanu"),
        "North Tripura" to listOf("Dharmanagar", "Kanchanpur", "Panisagar"),
        "South Tripura" to listOf("Udaipur", "Belonia", "Santirbazar"),
        "West Tripura" to listOf("Agartala", "Mohanpur", "Bishalgarh", "Sonamura"),

        // ========== UTTAR PRADESH ==========
        "Agra" to listOf("Agra", "Firozabad", "Fatehpur Sikri", "Kheragarh"),
        "Aligarh" to listOf("Aligarh", "Khair", "Atrauli", "Iglas"),
        "Allahabad" to listOf("Prayagraj", "Naini", "Phulpur", "Soraon"),
        "Bareilly" to listOf("Bareilly", "Baheri", "Faridpur", "Aonla"),
        "Ghaziabad" to listOf("Ghaziabad", "Modinagar", "Loni", "Murad Nagar"),
        "Gorakhpur" to listOf("Gorakhpur", "Gola Gokarannath", "Chauri Chaura"),
        "Jhansi" to listOf("Jhansi", "Lalitpur", "Mauranipur"),
        "Kanpur Nagar" to listOf("Kanpur", "Bithoor", "Ghatampur"),
        "Lucknow" to listOf("Lucknow", "Mohanlalganj", "Malihabad", "Bakshi Ka Talab"),
        "Mathura" to listOf("Mathura", "Vrindavan", "Govardhan", "Baldeo"),
        "Meerut" to listOf("Meerut", "Sardhana", "Mawana", "Daurala"),
        "Moradabad" to listOf("Moradabad", "Chandausi", "Sambhal"),
        "Noida" to listOf("Noida", "Greater Noida", "Dadri", "Jewar"),
        "Varanasi" to listOf("Varanasi", "Ramnagar", "Pindra", "Cholapur"),

        // ========== UTTARAKHAND ==========
        "Chamoli" to listOf("Gopeshwar", "Joshimath", "Karnaprayag"),
        "Dehradun" to listOf("Dehradun", "Mussoorie", "Rishikesh", "Doiwala", "Vikasnagar"),
        "Haridwar" to listOf("Haridwar", "Roorkee", "Jwalapur", "Laksar", "Bhagwanpur"),
        "Nainital" to listOf("Nainital", "Haldwani", "Ramnagar", "Bhimtal", "Lalkuan"),
        "Udham Singh Nagar" to listOf("Rudrapur", "Kashipur", "Jaspur", "Kichha", "Sitarganj"),

        // ========== WEST BENGAL ==========
        "Bankura" to listOf("Bankura", "Bishnupur", "Sonamukhi"),
        "Bardhaman" to listOf("Bardhaman", "Durgapur", "Asansol", "Kulti", "Raniganj"),
        "Darjeeling" to listOf("Darjeeling", "Siliguri", "Kurseong", "Mirik", "Kalimpong"),
        "Hooghly" to listOf("Chinsurah", "Serampore", "Chandannagar", "Bansberia", "Arambagh"),
        "Howrah" to listOf("Howrah", "Uluberia", "Domjur", "Shyampur"),
        "Kolkata" to listOf("Kolkata", "Salt Lake", "New Town", "Behala", "Jadavpur"),
        "Malda" to listOf("English Bazar", "Old Malda", "Gazole"),
        "Murshidabad" to listOf("Berhampore", "Lalbag", "Jangipur", "Kandi"),
        "Nadia" to listOf("Krishnanagar", "Kalyani", "Ranaghat", "Nabadwip"),
        "North 24 Parganas" to listOf("Barasat", "Barrackpore", "Basirhat", "Bongaon"),
        "South 24 Parganas" to listOf("Alipore", "Diamond Harbour", "Baruipur", "Kakdwip"),

        // ========== UNION TERRITORIES ==========
        "Nicobar" to listOf("Car Nicobar", "Nancowry", "Campbell Bay"),
        "North and Middle Andaman" to listOf("Mayabunder", "Diglipur", "Rangat"),
        "South Andaman" to listOf("Port Blair", "Garacharma", "Prothrapur"),
        "Chandigarh" to listOf("Chandigarh", "Manimajra", "Daria"),
        "Dadra and Nagar Haveli" to listOf("Silvassa", "Amli", "Naroli"),
        "Daman" to listOf("Daman", "Nani Daman", "Moti Daman"),
        "Diu" to listOf("Diu", "Ghoghla", "Bucharwada"),
        "Central Delhi" to listOf("Connaught Place", "Karol Bagh", "Paharganj", "Daryaganj"),
        "East Delhi" to listOf("Preet Vihar", "Laxmi Nagar", "Patparganj", "Mayur Vihar"),
        "New Delhi" to listOf("India Gate", "Lodhi Road", "Chanakyapuri", "Sarojini Nagar"),
        "North Delhi" to listOf("Civil Lines", "Model Town", "GTB Nagar", "Timarpur"),
        "North West Delhi" to listOf("Rohini", "Pitampura", "Shalimar Bagh", "Narela"),
        "South Delhi" to listOf("Hauz Khas", "Saket", "Greater Kailash", "Lajpat Nagar"),
        "South West Delhi" to listOf("Dwarka", "Vasant Kunj", "Janakpuri", "Najafgarh"),
        "West Delhi" to listOf("Rajouri Garden", "Patel Nagar", "Tilak Nagar", "Vikaspuri"),
        "Anantnag" to listOf("Anantnag", "Pahalgam", "Kokernag"),
        "Baramulla" to listOf("Baramulla", "Sopore", "Pattan", "Uri"),
        "Jammu" to listOf("Jammu", "R.S. Pura", "Bishnah", "Akhnoor"),
        "Kathua" to listOf("Kathua", "Basholi", "Billawar"),
        "Pulwama" to listOf("Pulwama", "Awantipora", "Tral"),
        "Srinagar" to listOf("Srinagar", "Ganderbal", "Budgam", "Harwan"),
        "Udhampur" to listOf("Udhampur", "Ramnagar", "Chenani"),
        "Kargil" to listOf("Kargil", "Drass", "Sankoo"),
        "Leh" to listOf("Leh", "Diskit", "Nyoma"),
        "Lakshadweep" to listOf("Kavaratti", "Agatti", "Minicoy", "Amini"),
        "Karaikal" to listOf("Karaikal", "Thirunallar", "Nedungadu"),
        "Mahe" to listOf("Mahe"),
        "Puducherry" to listOf("Puducherry", "Ozhukarai", "Villianur", "Bahour"),
        "Yanam" to listOf("Yanam")
    )
}
