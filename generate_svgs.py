import os

images_dir = r"C:\Users\nihas\.gemini\antigravity\scratch\SmartCart\src\main\webapp\images"

svgs = {
    "tablet-pro11.svg": """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 160">
  <defs><linearGradient id="gtab" x1="0%" y1="0%" x2="100%" y2="100%"><stop offset="0%" stop-color="#475569"/><stop offset="100%" stop-color="#1e293b"/></linearGradient></defs>
  <rect x="45" y="15" width="110" height="125" rx="10" fill="url(#gtab)" stroke="#94a3b8" stroke-width="3"/>
  <rect x="52" y="22" width="96" height="110" rx="6" fill="#6366f1" opacity="0.9"/>
  <circle cx="100" cy="26" r="2" fill="#0f172a"/>
  <circle cx="100" cy="77" r="22" fill="#a5b4fc" opacity="0.6"/>
  <text x="100" y="152" font-family="sans-serif" font-size="11" font-weight="bold" fill="#0f172a" text-anchor="middle">AeroPad Pro 11</text>
</svg>""",
    "tablet-stylus.svg": """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 160">
  <defs><linearGradient id="gsty" x1="0%" y1="0%" x2="100%" y2="100%"><stop offset="0%" stop-color="#e2e8f0"/><stop offset="100%" stop-color="#cbd5e1"/></linearGradient></defs>
  <rect x="30" y="72" width="130" height="14" rx="7" fill="url(#gsty)" stroke="#64748b" stroke-width="2"/>
  <polygon points="160,72 178,79 160,86" fill="#0f172a"/>
  <rect x="40" y="75" width="12" height="8" rx="2" fill="#10b981"/>
  <text x="100" y="130" font-family="sans-serif" font-size="12" font-weight="bold" fill="#0f172a" text-anchor="middle">Magnetic Stylus Pen</text>
</svg>""",
    "watch-ultra.svg": """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 160">
  <defs><linearGradient id="gwatch" x1="0%" y1="0%" x2="100%" y2="100%"><stop offset="0%" stop-color="#f97316"/><stop offset="100%" stop-color="#ea580c"/></linearGradient></defs>
  <rect x="75" y="10" width="50" height="30" rx="6" fill="url(#gwatch)"/>
  <rect x="75" y="115" width="50" height="30" rx="6" fill="url(#gwatch)"/>
  <rect x="65" y="35" width="70" height="80" rx="18" fill="#0f172a" stroke="#e2e8f0" stroke-width="4"/>
  <circle cx="100" cy="75" r="26" fill="#1e293b" stroke="#10b981" stroke-width="3" stroke-dasharray="120 40"/>
  <text x="100" y="80" font-family="sans-serif" font-size="13" font-weight="bold" fill="#ffffff" text-anchor="middle">10:45</text>
  <text x="100" y="155" font-family="sans-serif" font-size="11" font-weight="bold" fill="#0f172a" text-anchor="middle">Watch Ultra</text>
</svg>""",
    "watch-fit.svg": """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 160">
  <rect x="82" y="10" width="36" height="30" rx="4" fill="#334155"/>
  <rect x="82" y="115" width="36" height="30" rx="4" fill="#334155"/>
  <rect x="74" y="35" width="52" height="80" rx="14" fill="#0f172a" stroke="#06b6d4" stroke-width="3"/>
  <text x="100" y="72" font-family="sans-serif" font-size="16" font-weight="bold" fill="#38bdf8" text-anchor="middle">8,420</text>
  <text x="100" y="88" font-family="sans-serif" font-size="9" fill="#94a3b8" text-anchor="middle">STEPS</text>
  <text x="100" y="155" font-family="sans-serif" font-size="11" font-weight="bold" fill="#0f172a" text-anchor="middle">FitPulse Pro</text>
</svg>""",
    "powerbank-20k.svg": """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 160">
  <defs><linearGradient id="gpb" x1="0%" y1="0%" x2="100%" y2="100%"><stop offset="0%" stop-color="#1e293b"/><stop offset="100%" stop-color="#0f172a"/></linearGradient></defs>
  <rect x="45" y="30" width="110" height="75" rx="12" fill="url(#gpb)" stroke="#475569" stroke-width="3"/>
  <circle cx="65" cy="65" r="4" fill="#10b981"/>
  <circle cx="77" cy="65" r="4" fill="#10b981"/>
  <circle cx="89" cy="65" r="4" fill="#10b981"/>
  <circle cx="101" cy="65" r="4" fill="#64748b"/>
  <rect x="120" y="58" width="20" height="8" rx="2" fill="#38bdf8"/>
  <text x="100" y="92" font-family="sans-serif" font-size="11" font-weight="bold" fill="#f8fafc" text-anchor="middle">20,000mAh 65W</text>
  <text x="100" y="135" font-family="sans-serif" font-size="11" font-weight="bold" fill="#0f172a" text-anchor="middle">Fast Power Bank</text>
</svg>""",
    "adapter-dac.svg": """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 160">
  <rect x="35" y="68" width="30" height="20" rx="5" fill="#94a3b8" stroke="#475569" stroke-width="2"/>
  <path d="M65 78 C90 78, 105 78, 125 78" stroke="#334155" stroke-width="6" fill="none" stroke-linecap="round"/>
  <rect x="125" y="64" width="40" height="28" rx="6" fill="#0f172a" stroke="#10b981" stroke-width="2"/>
  <circle cx="152" cy="78" r="6" fill="#475569"/>
  <circle cx="152" cy="78" r="3" fill="#000000"/>
  <text x="100" y="130" font-family="sans-serif" font-size="12" font-weight="bold" fill="#0f172a" text-anchor="middle">Hi-Res DAC Dongle</text>
</svg>""",
    "monitor-4k.svg": """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 160">
  <rect x="25" y="15" width="150" height="95" rx="4" fill="#0f172a" stroke="#334155" stroke-width="3"/>
  <rect x="30" y="20" width="140" height="85" rx="2" fill="#1e1b4b"/>
  <path d="M30 75 Q100 35 170 85 L170 105 L30 105 Z" fill="#4f46e5" opacity="0.7"/>
  <rect x="92" y="110" width="16" height="20" fill="#64748b"/>
  <rect x="65" y="130" width="70" height="8" rx="3" fill="#475569"/>
  <text x="100" y="155" font-family="sans-serif" font-size="11" font-weight="bold" fill="#0f172a" text-anchor="middle">27" 4K 144Hz Monitor</text>
</svg>""",
    "keyboard-mech.svg": """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 160">
  <rect x="20" y="45" width="160" height="70" rx="8" fill="#1e293b" stroke="#475569" stroke-width="3"/>
  <rect x="28" y="53" width="144" height="54" rx="4" fill="#0f172a"/>
  <g fill="#38bdf8" opacity="0.8">
    <rect x="34" y="58" width="12" height="10" rx="2"/>
    <rect x="50" y="58" width="12" height="10" rx="2"/>
    <rect x="66" y="58" width="12" height="10" rx="2"/>
    <rect x="82" y="58" width="12" height="10" rx="2" fill="#ec4899"/>
    <rect x="98" y="58" width="12" height="10" rx="2"/>
    <rect x="114" y="58" width="12" height="10" rx="2" fill="#10b981"/>
    <rect x="130" y="58" width="12" height="10" rx="2"/>
    <rect x="146" y="58" width="20" height="10" rx="2" fill="#f59e0b"/>
    <rect x="34" y="72" width="18" height="10" rx="2"/>
    <rect x="56" y="72" width="12" height="10" rx="2"/>
    <rect x="72" y="72" width="12" height="10" rx="2"/>
    <rect x="88" y="72" width="12" height="10" rx="2"/>
    <rect x="104" y="72" width="12" height="10" rx="2"/>
    <rect x="120" y="72" width="12" height="10" rx="2"/>
    <rect x="136" y="72" width="30" height="10" rx="2" fill="#a855f7"/>
    <rect x="60" y="86" width="65" height="12" rx="2" fill="#6366f1"/>
  </g>
  <text x="100" y="145" font-family="sans-serif" font-size="11" font-weight="bold" fill="#0f172a" text-anchor="middle">RGB Mechanical Keyboard</text>
</svg>""",
    "mouse-ergo.svg": """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 160">
  <path d="M80 30 C110 30, 130 55, 130 95 C130 120, 115 130, 85 130 C65 130, 55 115, 60 90 C65 70, 60 45, 80 30 Z" fill="#1e293b" stroke="#475569" stroke-width="3"/>
  <line x1="88" y1="32" x2="88" y2="68" stroke="#64748b" stroke-width="2"/>
  <rect x="85" y="45" width="6" height="14" rx="3" fill="#10b981"/>
  <path d="M58 85 C52 90, 50 102, 58 112" stroke="#06b6d4" stroke-width="3" fill="none"/>
  <text x="100" y="152" font-family="sans-serif" font-size="11" font-weight="bold" fill="#0f172a" text-anchor="middle">Ergonomic Wireless Mouse</text>
</svg>"""
}

for name, content in svgs.items():
    p = os.path.join(images_dir, name)
    with open(p, "w", encoding="utf-8") as f:
        f.write(content)
    print(f"Generated {name}")
