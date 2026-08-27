// ============================================
// VitalMatch - Indian States, Districts & Cities
// Cascading dropdown data + utility functions
// ============================================

const INDIA_LOCATIONS = {
    "Andhra Pradesh": {
        "Anantapur": ["Anantapur", "Hindupur", "Guntakal", "Dharmavaram", "Tadpatri"],
        "Chittoor": ["Tirupati", "Chittoor", "Madanapalle", "Srikalahasti", "Punganur"],
        "East Godavari": ["Kakinada", "Rajahmundry", "Samalkot", "Amalapuram", "Peddapuram"],
        "Guntur": ["Guntur", "Tenali", "Narasaraopet", "Mangalagiri", "Bapatla"],
        "Krishna": ["Vijayawada", "Machilipatnam", "Gudivada", "Nuzvid", "Jaggaiahpet"],
        "Kurnool": ["Kurnool", "Nandyal", "Adoni", "Yemmiganur", "Atmakur"],
        "Nellore": ["Nellore", "Kavali", "Gudur", "Sullurpeta", "Atmakur"],
        "Prakasam": ["Ongole", "Chirala", "Markapur", "Kandukur", "Darsi"],
        "Srikakulam": ["Srikakulam", "Narasannapeta", "Amadalavalasa", "Palasa"],
        "Visakhapatnam": ["Visakhapatnam", "Gajuwaka", "Anakapalle", "Bheemunipatnam", "Narsipatnam"],
        "Vizianagaram": ["Vizianagaram", "Bobbili", "Rajam", "Parvathipuram"],
        "West Godavari": ["Eluru", "Bhimavaram", "Tadepalligudem", "Tanuku", "Narsapuram"],
        "YSR Kadapa": ["Kadapa", "Proddatur", "Rajampet", "Mydukur", "Jammalamadugu"]
    },
    "Arunachal Pradesh": {
        "Itanagar Capital Complex": ["Itanagar", "Naharlagun", "Banderdewa"],
        "Tawang": ["Tawang", "Lumla", "Jang"],
        "West Kameng": ["Bomdila", "Dirang", "Kalaktang"],
        "Papum Pare": ["Yupia", "Doimukh", "Balijan"],
        "Lower Subansiri": ["Ziro", "Raga", "Old Ziro"]
    },
    "Assam": {
        "Baksa": ["Mushalpur", "Tamulpur", "Barama"],
        "Cachar": ["Silchar", "Lakhipur", "Sonai"],
        "Dibrugarh": ["Dibrugarh", "Naharkatia", "Chabua", "Barbaruah"],
        "Kamrup Metropolitan": ["Guwahati", "Dispur", "North Guwahati", "Paltan Bazaar"],
        "Kamrup": ["Amingaon", "Rangia", "Boko", "Hajo"],
        "Jorhat": ["Jorhat", "Titabor", "Mariani"],
        "Nagaon": ["Nagaon", "Hojai", "Lanka", "Dhing"],
        "Sonitpur": ["Tezpur", "Rangapara", "Dhekiajuli"],
        "Tinsukia": ["Tinsukia", "Digboi", "Margherita", "Doomdooma"]
    },
    "Bihar": {
        "Araria": ["Araria", "Forbesganj", "Raniganj"],
        "Bhagalpur": ["Bhagalpur", "Sultanganj", "Naugachia"],
        "Gaya": ["Gaya", "Bodhgaya", "Sherghati", "Tekari"],
        "Muzaffarpur": ["Muzaffarpur", "Motipur", "Sitamarhi"],
        "Nalanda": ["Bihar Sharif", "Rajgir", "Hilsa"],
        "Patna": ["Patna", "Danapur", "Phulwari", "Masaurhi", "Barh"],
        "Purnia": ["Purnia", "Banmankhi", "Kasba"],
        "Saran": ["Chhapra", "Marhaura", "Revelganj"],
        "Vaishali": ["Hajipur", "Mahua", "Lalganj"]
    },
    "Chhattisgarh": {
        "Bastar": ["Jagdalpur", "Lohandiguda", "Bakawand"],
        "Bilaspur": ["Bilaspur", "Ratanpur", "Takhatpur", "Lormi"],
        "Durg": ["Durg", "Bhilai", "Patan", "Dhamdha"],
        "Korba": ["Korba", "Katghora", "Pali"],
        "Raigarh": ["Raigarh", "Sarangarh", "Gharghoda"],
        "Raipur": ["Raipur", "Abhanpur", "Arang", "Tilda"],
        "Rajnandgaon": ["Rajnandgaon", "Dongargarh", "Khairagarh"]
    },
    "Goa": {
        "North Goa": ["Panaji", "Mapusa", "Bicholim", "Valpoi", "Ponda"],
        "South Goa": ["Margao", "Vasco da Gama", "Cuncolim", "Quepem", "Canacona"]
    },
    "Gujarat": {
        "Ahmedabad": ["Ahmedabad", "Dhandhuka", "Bavla", "Sanand", "Viramgam"],
        "Amreli": ["Amreli", "Rajula", "Savarkundla"],
        "Anand": ["Anand", "Borsad", "Petlad", "Khambhat"],
        "Bhavnagar": ["Bhavnagar", "Palitana", "Sihor", "Mahuva"],
        "Gandhinagar": ["Gandhinagar", "Kalol", "Dehgam", "Mansa"],
        "Jamnagar": ["Jamnagar", "Dhrol", "Jodia", "Kalavad"],
        "Junagadh": ["Junagadh", "Veraval", "Manavadar", "Vanthali"],
        "Kutch": ["Bhuj", "Gandhidham", "Anjar", "Mundra", "Mandvi"],
        "Rajkot": ["Rajkot", "Gondal", "Jetpur", "Morbi"],
        "Surat": ["Surat", "Bardoli", "Mandvi", "Olpad", "Kamrej"],
        "Vadodara": ["Vadodara", "Dabhoi", "Padra", "Savli"]
    },
    "Haryana": {
        "Ambala": ["Ambala", "Barara", "Naraingarh"],
        "Faridabad": ["Faridabad", "Ballabgarh", "Palwal"],
        "Gurugram": ["Gurugram", "Sohna", "Pataudi", "Manesar"],
        "Hisar": ["Hisar", "Hansi", "Barwala"],
        "Karnal": ["Karnal", "Gharaunda", "Nilokheri", "Indri"],
        "Kurukshetra": ["Kurukshetra", "Thanesar", "Pehowa", "Ladwa"],
        "Panipat": ["Panipat", "Samalkha", "Israna"],
        "Rohtak": ["Rohtak", "Kalanaur", "Meham"],
        "Sonipat": ["Sonipat", "Ganaur", "Gohana"]
    },
    "Himachal Pradesh": {
        "Bilaspur": ["Bilaspur", "Ghumarwin", "Naina Devi"],
        "Kangra": ["Dharamshala", "Kangra", "Palampur", "Baijnath"],
        "Kullu": ["Kullu", "Manali", "Bhuntar", "Banjar"],
        "Mandi": ["Mandi", "Sundernagar", "Jogindernagar"],
        "Shimla": ["Shimla", "Theog", "Rampur Bushahr", "Kufri"],
        "Solan": ["Solan", "Nalagarh", "Baddi", "Parwanoo"]
    },
    "Jharkhand": {
        "Bokaro": ["Bokaro Steel City", "Chas", "Gomia"],
        "Dhanbad": ["Dhanbad", "Jharia", "Sindri", "Katras"],
        "East Singhbhum": ["Jamshedpur", "Gamharia", "Jugsalai", "Mango"],
        "Hazaribagh": ["Hazaribagh", "Barhi", "Ichak"],
        "Ranchi": ["Ranchi", "Namkum", "Kanke", "Ratu"],
        "West Singhbhum": ["Chaibasa", "Chakradharpur", "Jagannathpur"]
    },
    "Karnataka": {
        "Bagalkot": ["Bagalkot", "Badami", "Jamkhandi", "Mudhol"],
        "Bangalore Rural": ["Devanahalli", "Doddaballapur", "Nelamangala", "Hoskote"],
        "Bangalore Urban": ["Bengaluru", "Yelahanka", "Anekal", "Whitefield", "Electronic City"],
        "Belgaum": ["Belgaum", "Gokak", "Athani", "Chikkodi", "Raibag"],
        "Bellary": ["Bellary", "Hospet", "Sandur", "Siruguppa"],
        "Dakshina Kannada": ["Mangaluru", "Puttur", "Bantwal", "Sullia", "Belthangady"],
        "Dharwad": ["Dharwad", "Hubli", "Kalghatgi", "Kundgol"],
        "Gulbarga": ["Kalaburagi", "Sedam", "Aland", "Chincholi"],
        "Hassan": ["Hassan", "Arsikere", "Channarayapatna", "Belur"],
        "Mysuru": ["Mysuru", "Nanjangud", "T. Narasipura", "Hunsur", "K.R. Nagar"],
        "Raichur": ["Raichur", "Manvi", "Sindhanur", "Devadurga"],
        "Shimoga": ["Shimoga", "Bhadravathi", "Sagar", "Thirthahalli"],
        "Tumkur": ["Tumkur", "Tiptur", "Sira", "Madhugiri"],
        "Udupi": ["Udupi", "Kundapura", "Karkala", "Brahmavara"]
    },
    "Kerala": {
        "Alappuzha": ["Alappuzha", "Cherthala", "Kayamkulam", "Haripad"],
        "Ernakulam": ["Kochi", "Aluva", "Angamaly", "Perumbavoor", "Muvattupuzha"],
        "Idukki": ["Painavu", "Thodupuzha", "Munnar", "Adimali"],
        "Kannur": ["Kannur", "Thalassery", "Payyanur", "Mattannur"],
        "Kasaragod": ["Kasaragod", "Kanhangad", "Nileshwar"],
        "Kollam": ["Kollam", "Karunagappally", "Punalur", "Kottarakkara"],
        "Kottayam": ["Kottayam", "Pala", "Changanassery", "Vaikom"],
        "Kozhikode": ["Kozhikode", "Vadakara", "Koyilandy", "Feroke"],
        "Malappuram": ["Malappuram", "Manjeri", "Perinthalmanna", "Tirur", "Ponnani"],
        "Palakkad": ["Palakkad", "Ottapalam", "Shoranur", "Chittur", "Mannarkkad"],
        "Pathanamthitta": ["Pathanamthitta", "Adoor", "Thiruvalla", "Ranni"],
        "Thiruvananthapuram": ["Thiruvananthapuram", "Neyyattinkara", "Attingal", "Nedumangad"],
        "Thrissur": ["Thrissur", "Guruvayur", "Chalakudy", "Kunnamkulam", "Irinjalakuda"],
        "Wayanad": ["Kalpetta", "Sulthan Bathery", "Mananthavady"]
    },
    "Madhya Pradesh": {
        "Bhopal": ["Bhopal", "Berasia", "Sehore"],
        "Gwalior": ["Gwalior", "Dabra", "Bhitarwar"],
        "Indore": ["Indore", "Mhow", "Sanwer", "Depalpur"],
        "Jabalpur": ["Jabalpur", "Sihora", "Patan"],
        "Rewa": ["Rewa", "Mauganj", "Hanumana"],
        "Sagar": ["Sagar", "Khurai", "Bina", "Banda"],
        "Satna": ["Satna", "Maihar", "Amarpatan"],
        "Ujjain": ["Ujjain", "Nagda", "Mahidpur", "Tarana"]
    },
    "Maharashtra": {
        "Ahmednagar": ["Ahmednagar", "Shrirampur", "Rahuri", "Sangamner"],
        "Aurangabad": ["Aurangabad", "Paithan", "Vaijapur", "Kannad"],
        "Kolhapur": ["Kolhapur", "Ichalkaranji", "Jaysingpur", "Gadhinglaj"],
        "Mumbai City": ["Mumbai", "Colaba", "Dadar", "Byculla", "Worli"],
        "Mumbai Suburban": ["Andheri", "Borivali", "Kandivali", "Malad", "Goregaon"],
        "Nagpur": ["Nagpur", "Kamptee", "Hingna", "Narkhed"],
        "Nashik": ["Nashik", "Malegaon", "Igatpuri", "Sinnar", "Manmad"],
        "Pune": ["Pune", "Pimpri-Chinchwad", "Baramati", "Junnar", "Shirur"],
        "Raigad": ["Alibag", "Panvel", "Pen", "Khalapur", "Karjat"],
        "Sangli": ["Sangli", "Miraj", "Tasgaon", "Vita"],
        "Satara": ["Satara", "Karad", "Wai", "Mahabaleshwar"],
        "Solapur": ["Solapur", "Pandharpur", "Barshi", "Akkalkot"],
        "Thane": ["Thane", "Kalyan", "Dombivli", "Ulhasnagar", "Bhiwandi"]
    },
    "Manipur": {
        "Bishnupur": ["Bishnupur", "Moirang", "Ningthoukhong"],
        "Imphal East": ["Porompat", "Jiribam", "Lamlai"],
        "Imphal West": ["Imphal", "Lamphel", "Singjamei"],
        "Thoubal": ["Thoubal", "Kakching", "Lilong"]
    },
    "Meghalaya": {
        "East Garo Hills": ["Williamnagar", "Rongjeng", "Songsak"],
        "East Khasi Hills": ["Shillong", "Nongpoh", "Cherrapunji", "Mawkyrwat"],
        "West Garo Hills": ["Tura", "Dadenggre", "Selsella"],
        "Ri-Bhoi": ["Nongpoh", "Umroi", "Byrnihat"]
    },
    "Mizoram": {
        "Aizawl": ["Aizawl", "Darlawn", "Saitual"],
        "Lunglei": ["Lunglei", "Hnahthial", "Lawngtlai"],
        "Champhai": ["Champhai", "Khawzawl", "Biate"]
    },
    "Nagaland": {
        "Dimapur": ["Dimapur", "Chumukedima", "Medziphema"],
        "Kohima": ["Kohima", "Tseminyu", "Chiephobozou"],
        "Mokokchung": ["Mokokchung", "Tuli", "Changtongya"]
    },
    "Odisha": {
        "Balasore": ["Balasore", "Jaleswar", "Soro", "Nilgiri"],
        "Cuttack": ["Cuttack", "Choudwar", "Banki", "Athagarh"],
        "Ganjam": ["Berhampur", "Chatrapur", "Aska", "Hinjilicut"],
        "Khordha": ["Bhubaneswar", "Jatni", "Balugaon", "Khordha"],
        "Koraput": ["Koraput", "Jeypore", "Sunabeda"],
        "Mayurbhanj": ["Baripada", "Rairangpur", "Karanjia"],
        "Puri": ["Puri", "Konark", "Pipili", "Nimapara"],
        "Sambalpur": ["Sambalpur", "Burla", "Hirakud", "Kuchinda"],
        "Sundargarh": ["Rourkela", "Sundargarh", "Rajgangpur", "Bonai"]
    },
    "Punjab": {
        "Amritsar": ["Amritsar", "Ajnala", "Baba Bakala", "Tarn Taran"],
        "Bathinda": ["Bathinda", "Rampura Phul", "Maur", "Talwandi Sabo"],
        "Jalandhar": ["Jalandhar", "Phagwara", "Nakodar", "Phillaur", "Kartarpur"],
        "Ludhiana": ["Ludhiana", "Khanna", "Jagraon", "Samrala", "Raikot"],
        "Mohali": ["Mohali", "Kharar", "Derabassi", "Zirakpur"],
        "Patiala": ["Patiala", "Rajpura", "Samana", "Nabha"],
        "Sangrur": ["Sangrur", "Malerkotla", "Barnala", "Dhuri"]
    },
    "Rajasthan": {
        "Ajmer": ["Ajmer", "Beawar", "Kishangarh", "Nasirabad"],
        "Alwar": ["Alwar", "Bhiwadi", "Behror", "Rajgarh"],
        "Bikaner": ["Bikaner", "Nokha", "Lunkaransar"],
        "Jaipur": ["Jaipur", "Sanganer", "Amber", "Chomu", "Chaksu"],
        "Jodhpur": ["Jodhpur", "Bilara", "Pipar City", "Phalodi"],
        "Kota": ["Kota", "Ramganj Mandi", "Sangod"],
        "Sikar": ["Sikar", "Fatehpur", "Lachhmangarh", "Neem Ka Thana"],
        "Udaipur": ["Udaipur", "Rajsamand", "Nathdwara", "Salumber"]
    },
    "Sikkim": {
        "East Sikkim": ["Gangtok", "Singtam", "Rangpo", "Pakyong"],
        "North Sikkim": ["Mangan", "Chungthang", "Lachung"],
        "South Sikkim": ["Namchi", "Jorethang", "Ravangla"],
        "West Sikkim": ["Gyalshing", "Pelling", "Soreng"]
    },
    "Tamil Nadu": {
        "Chennai": ["Chennai", "Tambaram", "Avadi", "Ambattur", "Chromepet"],
        "Coimbatore": ["Coimbatore", "Pollachi", "Mettupalayam", "Sulur", "Valparai"],
        "Cuddalore": ["Cuddalore", "Chidambaram", "Virudhachalam", "Neyveli"],
        "Erode": ["Erode", "Bhavani", "Gobichettipalayam", "Sathyamangalam"],
        "Kancheepuram": ["Kancheepuram", "Sriperumbudur", "Uthiramerur", "Walajabad"],
        "Kanyakumari": ["Nagercoil", "Marthandam", "Colachel", "Padmanabhapuram"],
        "Madurai": ["Madurai", "Melur", "Usilampatti", "Vadipatti", "Thirumangalam"],
        "Nagapattinam": ["Nagapattinam", "Sirkazhi", "Mayiladuthurai", "Tharangambadi"],
        "Salem": ["Salem", "Mettur", "Attur", "Omalur"],
        "Thanjavur": ["Thanjavur", "Kumbakonam", "Pattukkottai", "Orathanadu"],
        "Tiruchirappalli": ["Tiruchirappalli", "Srirangam", "Lalgudi", "Musiri", "Thuraiyur"],
        "Tirunelveli": ["Tirunelveli", "Palayamkottai", "Ambasamudram", "Tenkasi"],
        "Tiruppur": ["Tiruppur", "Avinashi", "Udumalpet", "Dharapuram", "Kangeyam"],
        "Tiruvallur": ["Tiruvallur", "Tiruttani", "Ponneri", "Gummidipoondi"],
        "Tiruvannamalai": ["Tiruvannamalai", "Arani", "Polur", "Chengam"],
        "Vellore": ["Vellore", "Ambur", "Gudiyatham", "Vaniyambadi", "Ranipet"],
        "Villupuram": ["Villupuram", "Tindivanam", "Gingee", "Kallakurichi"]
    },
    "Telangana": {
        "Adilabad": ["Adilabad", "Nirmal", "Mancherial", "Bellampally"],
        "Hyderabad": ["Hyderabad", "Secunderabad", "Charminar", "Begumpet"],
        "Karimnagar": ["Karimnagar", "Jagtial", "Peddapalli", "Korutla"],
        "Khammam": ["Khammam", "Kothagudem", "Yellandu", "Sathupalli"],
        "Mahabubnagar": ["Mahabubnagar", "Jadcherla", "Narayanpet", "Wanaparthy"],
        "Medak": ["Sangareddy", "Medak", "Siddipet", "Zahirabad"],
        "Nalgonda": ["Nalgonda", "Suryapet", "Miryalaguda", "Devarakonda"],
        "Nizamabad": ["Nizamabad", "Kamareddy", "Bodhan", "Armoor"],
        "Rangareddy": ["Shamshabad", "Rajendra Nagar", "Chevella", "Ibrahimpatnam"],
        "Warangal": ["Warangal", "Hanamkonda", "Kazipet", "Jangaon"]
    },
    "Tripura": {
        "Dhalai": ["Ambassa", "Kamalpur", "Chawmanu"],
        "North Tripura": ["Dharmanagar", "Kanchanpur", "Panisagar"],
        "South Tripura": ["Udaipur", "Belonia", "Santirbazar"],
        "West Tripura": ["Agartala", "Mohanpur", "Bishalgarh", "Sonamura"]
    },
    "Uttar Pradesh": {
        "Agra": ["Agra", "Firozabad", "Fatehpur Sikri", "Kheragarh"],
        "Aligarh": ["Aligarh", "Khair", "Atrauli", "Iglas"],
        "Allahabad": ["Prayagraj", "Naini", "Phulpur", "Soraon"],
        "Bareilly": ["Bareilly", "Baheri", "Faridpur", "Aonla"],
        "Ghaziabad": ["Ghaziabad", "Modinagar", "Loni", "Murad Nagar"],
        "Gorakhpur": ["Gorakhpur", "Gola Gokarannath", "Chauri Chaura"],
        "Jhansi": ["Jhansi", "Lalitpur", "Mauranipur"],
        "Kanpur Nagar": ["Kanpur", "Bithoor", "Ghatampur"],
        "Lucknow": ["Lucknow", "Mohanlalganj", "Malihabad", "Bakshi Ka Talab"],
        "Mathura": ["Mathura", "Vrindavan", "Govardhan", "Baldeo"],
        "Meerut": ["Meerut", "Sardhana", "Mawana", "Daurala"],
        "Moradabad": ["Moradabad", "Chandausi", "Sambhal"],
        "Noida": ["Noida", "Greater Noida", "Dadri", "Jewar"],
        "Varanasi": ["Varanasi", "Ramnagar", "Pindra", "Cholapur"]
    },
    "Uttarakhand": {
        "Chamoli": ["Gopeshwar", "Joshimath", "Karnaprayag"],
        "Dehradun": ["Dehradun", "Mussoorie", "Rishikesh", "Doiwala", "Vikasnagar"],
        "Haridwar": ["Haridwar", "Roorkee", "Jwalapur", "Laksar", "Bhagwanpur"],
        "Nainital": ["Nainital", "Haldwani", "Ramnagar", "Bhimtal", "Lalkuan"],
        "Udham Singh Nagar": ["Rudrapur", "Kashipur", "Jaspur", "Kichha", "Sitarganj"]
    },
    "West Bengal": {
        "Bankura": ["Bankura", "Bishnupur", "Sonamukhi"],
        "Bardhaman": ["Bardhaman", "Durgapur", "Asansol", "Kulti", "Raniganj"],
        "Darjeeling": ["Darjeeling", "Siliguri", "Kurseong", "Mirik", "Kalimpong"],
        "Hooghly": ["Chinsurah", "Serampore", "Chandannagar", "Bansberia", "Arambagh"],
        "Howrah": ["Howrah", "Uluberia", "Domjur", "Shyampur"],
        "Kolkata": ["Kolkata", "Salt Lake", "New Town", "Behala", "Jadavpur"],
        "Malda": ["English Bazar", "Old Malda", "Gazole"],
        "Murshidabad": ["Berhampore", "Lalbag", "Jangipur", "Kandi"],
        "Nadia": ["Krishnanagar", "Kalyani", "Ranaghat", "Nabadwip"],
        "North 24 Parganas": ["Barasat", "Barrackpore", "Basirhat", "Bongaon"],
        "South 24 Parganas": ["Alipore", "Diamond Harbour", "Baruipur", "Kakdwip"]
    },
    "Andaman and Nicobar Islands": {
        "Nicobar": ["Car Nicobar", "Nancowry", "Campbell Bay"],
        "North and Middle Andaman": ["Mayabunder", "Diglipur", "Rangat"],
        "South Andaman": ["Port Blair", "Garacharma", "Prothrapur"]
    },
    "Chandigarh": {
        "Chandigarh": ["Chandigarh", "Manimajra", "Daria"]
    },
    "Dadra and Nagar Haveli and Daman and Diu": {
        "Dadra and Nagar Haveli": ["Silvassa", "Amli", "Naroli"],
        "Daman": ["Daman", "Nani Daman", "Moti Daman"],
        "Diu": ["Diu", "Ghoghla", "Bucharwada"]
    },
    "Delhi": {
        "Central Delhi": ["Connaught Place", "Karol Bagh", "Paharganj", "Daryaganj"],
        "East Delhi": ["Preet Vihar", "Laxmi Nagar", "Patparganj", "Mayur Vihar"],
        "New Delhi": ["India Gate", "Lodhi Road", "Chanakyapuri", "Sarojini Nagar"],
        "North Delhi": ["Civil Lines", "Model Town", "GTB Nagar", "Timarpur"],
        "North West Delhi": ["Rohini", "Pitampura", "Shalimar Bagh", "Narela"],
        "South Delhi": ["Hauz Khas", "Saket", "Greater Kailash", "Lajpat Nagar"],
        "South West Delhi": ["Dwarka", "Vasant Kunj", "Janakpuri", "Najafgarh"],
        "West Delhi": ["Rajouri Garden", "Patel Nagar", "Tilak Nagar", "Vikaspuri"]
    },
    "Jammu and Kashmir": {
        "Anantnag": ["Anantnag", "Pahalgam", "Kokernag"],
        "Baramulla": ["Baramulla", "Sopore", "Pattan", "Uri"],
        "Jammu": ["Jammu", "R.S. Pura", "Bishnah", "Akhnoor"],
        "Kathua": ["Kathua", "Basholi", "Billawar"],
        "Pulwama": ["Pulwama", "Awantipora", "Tral"],
        "Srinagar": ["Srinagar", "Ganderbal", "Budgam", "Harwan"],
        "Udhampur": ["Udhampur", "Ramnagar", "Chenani"]
    },
    "Ladakh": {
        "Kargil": ["Kargil", "Drass", "Sankoo"],
        "Leh": ["Leh", "Diskit", "Nyoma"]
    },
    "Lakshadweep": {
        "Lakshadweep": ["Kavaratti", "Agatti", "Minicoy", "Amini"]
    },
    "Puducherry": {
        "Karaikal": ["Karaikal", "Thirunallar", "Nedungadu"],
        "Mahe": ["Mahe"],
        "Puducherry": ["Puducherry", "Ozhukarai", "Villianur", "Bahour"],
        "Yanam": ["Yanam"]
    }
};

// ============================================
// Utility Functions for Cascading Dropdowns
// ============================================

function populateStates(selectElement) {
    selectElement.innerHTML = '<option value="" selected>Select State</option>';
    Object.keys(INDIA_LOCATIONS).sort().forEach(state => {
        const opt = document.createElement('option');
        opt.value = state;
        opt.textContent = state;
        selectElement.appendChild(opt);
    });
}

function populateDistricts(selectElement, state) {
    selectElement.innerHTML = '<option value="" selected>Select District</option>';
    if (!state || !INDIA_LOCATIONS[state]) return;
    Object.keys(INDIA_LOCATIONS[state]).sort().forEach(district => {
        const opt = document.createElement('option');
        opt.value = district;
        opt.textContent = district;
        selectElement.appendChild(opt);
    });
}

function populateCities(selectElement, state, district) {
    selectElement.innerHTML = '<option value="" selected>Select City</option>';
    if (!state || !district || !INDIA_LOCATIONS[state] || !INDIA_LOCATIONS[state][district]) return;
    INDIA_LOCATIONS[state][district].forEach(city => {
        const opt = document.createElement('option');
        opt.value = city;
        opt.textContent = city;
        selectElement.appendChild(opt);
    });
}

function setupCascadingDropdowns(stateId, districtId, cityId) {
    const stateEl = document.getElementById(stateId);
    const districtEl = document.getElementById(districtId);
    const cityEl = document.getElementById(cityId);

    populateStates(stateEl);

    stateEl.addEventListener('change', () => {
        populateDistricts(districtEl, stateEl.value);
        cityEl.innerHTML = '<option value="" selected>Select City</option>';
    });

    districtEl.addEventListener('change', () => {
        populateCities(cityEl, stateEl.value, districtEl.value);
    });
}
