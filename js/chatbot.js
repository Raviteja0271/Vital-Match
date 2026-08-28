// ============================================
// VitalMatch - Full Database NLP Chatbot Engine & Floating UI
// Requires: supabase-config.js + database.js
// ============================================

(function() {
    // 1. Inject Chatbot Styles
    const style = document.createElement('style');
    style.innerHTML = `
        .vm-chatbot-fab {
            position: fixed;
            bottom: 24px;
            right: 24px;
            width: 60px;
            height: 60px;
            border-radius: 50%;
            background: linear-gradient(135deg, #EF4444, #DC2626);
            color: #ffffff;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 10px 25px rgba(239, 68, 68, 0.4);
            cursor: pointer;
            z-index: 9999;
            transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
        }
        .vm-chatbot-fab:hover {
            transform: scale(1.1) rotate(5deg);
            box-shadow: 0 14px 30px rgba(239, 68, 68, 0.5);
        }
        .vm-chatbot-fab i {
            font-size: 26px;
        }

        .vm-chatbot-window {
            position: fixed;
            bottom: 96px;
            right: 24px;
            width: 400px;
            max-width: calc(100vw - 32px);
            height: 560px;
            max-height: calc(100vh - 120px);
            background: #ffffff;
            border-radius: 20px;
            box-shadow: 0 12px 40px rgba(0, 0, 0, 0.2);
            display: flex;
            flex-direction: column;
            overflow: hidden;
            z-index: 9999;
            opacity: 0;
            transform: translateY(20px) scale(0.95);
            pointer-events: none;
            transition: all 0.3s ease;
        }

        html.dark-mode .vm-chatbot-window {
            background: #1E293B;
            color: #F8FAFC;
            border: 1px solid rgba(255, 255, 255, 0.1);
        }

        .vm-chatbot-window.open {
            opacity: 1;
            transform: translateY(0) scale(1);
            pointer-events: all;
        }

        .vm-chatbot-header {
            background: linear-gradient(135deg, #EF4444, #DC2626);
            color: white;
            padding: 16px 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .vm-chatbot-body {
            flex: 1;
            padding: 16px;
            overflow-y: auto;
            display: flex;
            flex-direction: column;
            gap: 12px;
            background: #F8F9FA;
        }

        html.dark-mode .vm-chatbot-body {
            background: #0F172A;
        }

        .vm-chat-msg {
            max-width: 90%;
            padding: 12px 16px;
            border-radius: 16px;
            font-size: 13.5px;
            line-height: 1.5;
            word-wrap: break-word;
        }

        .vm-chat-msg.bot {
            background: #ffffff;
            color: #1E293B;
            align-self: flex-start;
            border-bottom-left-radius: 4px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        }

        html.dark-mode .vm-chat-msg.bot {
            background: #334155;
            color: #F8FAFC;
        }

        .vm-chat-msg.user {
            background: #EF4444;
            color: #ffffff;
            align-self: flex-end;
            border-bottom-right-radius: 4px;
        }

        .vm-chatbot-quick {
            display: flex;
            gap: 6px;
            overflow-x: auto;
            padding: 8px 12px;
            background: #ffffff;
            border-top: 1px solid #E2E8F0;
        }

        html.dark-mode .vm-chatbot-quick {
            background: #1E293B;
            border-top-color: #334155;
        }

        .vm-quick-chip {
            white-space: nowrap;
            font-size: 11px;
            font-weight: 600;
            padding: 6px 12px;
            border-radius: 20px;
            background: rgba(239, 68, 68, 0.1);
            color: #EF4444;
            border: 1px solid rgba(239, 68, 68, 0.2);
            cursor: pointer;
            transition: all 0.2s ease;
        }

        .vm-quick-chip:hover {
            background: #EF4444;
            color: #ffffff;
        }

        .vm-chatbot-footer {
            padding: 12px 16px;
            background: #ffffff;
            border-top: 1px solid #E2E8F0;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        html.dark-mode .vm-chatbot-footer {
            background: #1E293B;
            border-top-color: #334155;
        }

        .vm-chatbot-input {
            flex: 1;
            border: 1px solid #CBD5E1;
            border-radius: 24px;
            padding: 10px 16px;
            font-size: 13px;
            outline: none;
            background: #F8FAFC;
        }

        html.dark-mode .vm-chatbot-input {
            background: #0F172A;
            border-color: #334155;
            color: #ffffff;
        }

        .vm-chatbot-send {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: #EF4444;
            color: white;
            border: none;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: background 0.2s ease;
        }

        .vm-chatbot-send:hover {
            background: #DC2626;
        }
    `;
    document.head.appendChild(style);

    // 2. Inject Chatbot HTML Elements
    const container = document.createElement('div');
    container.innerHTML = `
        <div class="vm-chatbot-fab" id="vmChatFab" title="VitalMatch AI Assistant">
            <i class="fas fa-robot"></i>
        </div>

        <div class="vm-chatbot-window" id="vmChatWindow">
            <div class="vm-chatbot-header">
                <div class="d-flex align-items-center gap-2">
                    <i class="fas fa-robot fa-lg"></i>
                    <div>
                        <h6 class="fw-bold mb-0 text-white">VitalMatch AI Assistant</h6>
                        <small style="font-size:10px; opacity:0.8;">NLP Donor & Help Assistant</small>
                    </div>
                </div>
                <button class="btn btn-sm text-white" id="vmChatClose"><i class="fas fa-times"></i></button>
            </div>

            <div class="vm-chatbot-body" id="vmChatBody">
                <div class="vm-chat-msg bot">
                    Hello! 👋 I am VitalMatch AI Assistant.<br>
                    I can help you view all blood donors, search by location/blood group, post emergency requests, or check eligibility.<br><br>
                    Try asking:<br>
                    • <i>'Show All Donors'</i><br>
                    • <i>'Find Donors in Prakasam'</i><br>
                    • <i>'Search A+ Blood Donors'</i><br>
                    • <i>'How to post emergency?'</i>
                </div>
            </div>

            <div class="vm-chatbot-quick">
                <span class="vm-quick-chip" data-msg="Show All Donors">Show All Donors</span>
                <span class="vm-quick-chip" data-msg="Find Donors in Prakasam">Donors in Prakasam</span>
                <span class="vm-quick-chip" data-msg="Search A+ Blood Donors">A+ Blood Donors</span>
                <span class="vm-quick-chip" data-msg="How to post emergency?">Post Emergency</span>
            </div>

            <div class="vm-chatbot-footer">
                <input type="text" class="vm-chatbot-input" id="vmChatInput" placeholder="Ask for donors, location, blood group...">
                <button class="vm-chatbot-send" id="vmChatSend"><i class="fas fa-paper-plane"></i></button>
            </div>
        </div>
    `;
    document.body.appendChild(container);

    // 3. Logic & Event Listeners
    const fab = document.getElementById('vmChatFab');
    const win = document.getElementById('vmChatWindow');
    const closeBtn = document.getElementById('vmChatClose');
    const sendBtn = document.getElementById('vmChatSend');
    const inputEl = document.getElementById('vmChatInput');
    const bodyEl = document.getElementById('vmChatBody');

    fab.addEventListener('click', () => win.classList.toggle('open'));
    closeBtn.addEventListener('click', () => win.classList.remove('open'));

    document.querySelectorAll('.vm-quick-chip').forEach(chip => {
        chip.addEventListener('click', () => {
            const msg = chip.getAttribute('data-msg');
            inputEl.value = msg;
            handleSend();
        });
    });

    sendBtn.addEventListener('click', handleSend);
    inputEl.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') handleSend();
    });

    async function handleSend() {
        const text = inputEl.value.trim();
        if (!text) return;

        appendMsg('user', text);
        inputEl.value = '';

        const thinkingId = 'thinking_' + Date.now();
        appendMsg('bot', '<i class="fas fa-spinner fa-spin me-2"></i>Querying donor database...', thinkingId);

        const response = await processNlpQueryWeb(text);

        const thinkingEl = document.getElementById(thinkingId);
        if (thinkingEl) thinkingEl.remove();

        appendMsg('bot', response.html);
    }

    function appendMsg(sender, htmlContent, id = null) {
        const msgDiv = document.createElement('div');
        msgDiv.className = `vm-chat-msg ${sender}`;
        if (id) msgDiv.id = id;
        msgDiv.innerHTML = htmlContent;
        bodyEl.appendChild(msgDiv);
        bodyEl.scrollTop = bodyEl.scrollHeight;
    }

    // 4. Dynamic Database NLP Query Engine
    async function processNlpQueryWeb(query) {
        const q = query.toLowerCase().trim();

        // 1. Extract Blood Group
        const bloodMatch = q.match(/\b(a\+|a-|b\+|b-|o\+|o-|ab\+|ab-)\b/i);
        const extractedBlood = bloodMatch ? bloodMatch[0].toUpperCase() : null;

        // 2. Extract Location dynamically
        const stopWords = [
            "show", "find", "search", "list", "donors", "donor", "in", "for", "blood", "give",
            "all", "the", "me", "data", "please", "are", "available", "there", "any", "get",
            "database", "details", "info", "information", "registered", "need", "want", "of", "display"
        ];

        let locationQuery = q;
        if (extractedBlood) {
            locationQuery = locationQuery.replace(extractedBlood.toLowerCase(), '');
        }

        const cleanTokens = locationQuery.split(/\s+/).filter(t => t.length > 2 && !stopWords.includes(t));
        const extractedLocation = cleanTokens.join(' ').trim() || null;

        // 3. Query Supabase Database Profiles
        try {
            let { data: donors } = await supabaseClient.from('profiles').select('*').eq('is_donor', true);
            donors = donors || [];

            const isAllDataQuery = q.includes("all") || q.includes("database") || q.includes("every") ||
                (!extractedBlood && !extractedLocation && (q.includes("donor") || q.includes("list") || q.includes("data")));

            const filtered = (isAllDataQuery && !extractedBlood && !extractedLocation) ? donors : donors.filter(d => {
                const matchBlood = !extractedBlood || (d.blood_group && d.blood_group.toUpperCase() === extractedBlood);
                const matchLoc = !extractedLocation ||
                    (d.district && d.district.toLowerCase().includes(extractedLocation)) ||
                    (d.city && d.city.toLowerCase().includes(extractedLocation)) ||
                    (d.state && d.state.toLowerCase().includes(extractedLocation)) ||
                    (d.full_name && d.full_name.toLowerCase().includes(extractedLocation));
                return matchBlood && matchLoc;
            });

            if (filtered.length > 0) {
                let headerText = '';
                if (isAllDataQuery && !extractedBlood && !extractedLocation) {
                    headerText = `Displaying all ${filtered.length} registered donor(s) from Supabase database:`;
                } else if (extractedBlood && extractedLocation) {
                    headerText = `Found ${filtered.length} ${extractedBlood} donor(s) in ${extractedLocation.toUpperCase()}:`;
                } else if (extractedBlood) {
                    headerText = `Found ${filtered.length} ${extractedBlood} donor(s) in database:`;
                } else if (extractedLocation) {
                    headerText = `Found ${filtered.length} donor(s) in ${extractedLocation.toUpperCase()}:`;
                } else {
                    headerText = `Found ${filtered.length} donor(s) in database:`;
                }

                let html = `<p class="fw-bold mb-2 text-dark">${headerText}</p>`;

                filtered.forEach(d => {
                    const loc = [d.city, d.district, d.state].filter(Boolean).join(', ');
                    const isAvail = d.is_available !== false;
                    html += `
                        <div class="bg-light dark-mode-bg-dark p-2.5 rounded-3 mb-2 border">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <strong class="small text-dark">${d.full_name || 'Registered Donor'}</strong>
                                <span class="badge bg-danger">${d.blood_group || 'N/A'}</span>
                            </div>
                            <div class="text-muted smaller mb-1"><i class="fas fa-map-marker-alt me-1 text-primary"></i>${loc || 'Location Not Specified'}</div>
                            <div class="mb-2">
                                <span class="badge ${isAvail ? 'bg-success' : 'bg-secondary'} rounded-pill smaller">${isAvail ? 'Available' : 'Unavailable'}</span>
                            </div>
                            ${d.mobile ? `<a href="tel:${d.mobile}" class="btn btn-sm btn-outline-danger w-100 py-1 fw-bold smaller"><i class="fas fa-phone me-1"></i>Call Phone: ${d.mobile}</a>` : ''}
                        </div>
                    `;
                });

                return { html };
            } else {
                const locStr = extractedLocation ? extractedLocation.toUpperCase() : "that location";
                const bloodStr = extractedBlood || "that blood group";
                return {
                    html: `No registered ${bloodStr} donors found matching '${locStr}' in the database.<br><br>
                           <a href="post-emergency.html" class="btn btn-sm btn-danger fw-bold text-white mt-1 w-100"><i class="fas fa-exclamation-triangle me-1"></i>Post Emergency Request</a>`
                };
            }
        } catch (err) {
            if (q.includes("emergency") || q.includes("urgent") || q.includes("post")) {
                return {
                    html: `To post an urgent blood emergency:<br>
                           1. Tap 'Post Emergency'<br>
                           2. Fill hospital details & contact<br>
                           3. Donors in your district will be notified immediately!<br><br>
                           <a href="post-emergency.html" class="btn btn-sm btn-danger fw-bold text-white w-100"><i class="fas fa-ambulance me-1"></i>Post Emergency Now</a>`
                };
            }

            if (q.includes("eligible") || q.includes("rules") || q.includes("criteria") || q.includes("days")) {
                return {
                    html: `🩸 <b>Blood Donation Eligibility Criteria:</b><br>
                           • Age: 18 – 65 years<br>
                           • Weight: Minimum 45 kg<br>
                           • Frequency: Must wait 90 days between donations<br>
                           • Hemoglobin: At least 12.5 g/dL<br>
                           • Good general health condition.`
                };
            }

            return {
                html: `Try asking: <i>'Show All Donors'</i>, <i>'Find Donors in Prakasam'</i>, or <i>'How to post emergency?'</i>`
            };
        }
    }
})();
