import os
import sys
import json
import urllib.request
import urllib.parse
from PIL import Image, ImageDraw, ImageFont

IMAGES_DIR = r"C:\Users\nihas\.gemini\antigravity\scratch\SmartCart\src\main\webapp\images"
os.makedirs(IMAGES_DIR, exist_ok=True)

# Wikimedia image fetcher with timeout
def try_fetch_wikimedia(query, filename):
    filepath = os.path.join(IMAGES_DIR, filename)
    if os.path.exists(filepath) and os.path.getsize(filepath) > 5000:
        return True

    api_url = f"https://commons.wikimedia.org/w/api.php?action=query&generator=search&gsrsearch={urllib.parse.quote(query)}&gsrnamespace=6&gsrlimit=1&prop=imageinfo&iiprop=url&iiurlwidth=400&format=json"
    req = urllib.request.Request(api_url, headers={'User-Agent': 'SmartCartEcom/1.0 (academic; student@smartcart.in)'})
    try:
        with urllib.request.urlopen(req, timeout=4) as resp:
            data = json.loads(resp.read().decode())
            pages = data.get('query', {}).get('pages', {})
            for p in pages.values():
                thumb = p.get('imageinfo', [{}])[0].get('thumburl')
                if thumb and ('.jpg' in thumb.lower() or '.png' in thumb.lower()):
                    req_img = urllib.request.Request(thumb, headers={'User-Agent': 'SmartCartEcom/1.0 (academic; student@smartcart.in)'})
                    with urllib.request.urlopen(req_img, timeout=5) as img_resp:
                        img_bytes = img_resp.read()
                        if len(img_bytes) > 3000:
                            # Re-encode nicely onto studio canvas with clean border
                            with open(filepath, 'wb') as f:
                                f.write(img_bytes)
                            print(f"[WIKI DOWNLOAD] {filename} <- {query}")
                            return True
    except Exception as e:
        pass
    return False

def get_font(size):
    try:
        return ImageFont.truetype("arial.ttf", size)
    except:
        return ImageFont.load_default()

def create_studio_canvas():
    img = Image.new('RGBA', (400, 320), (248, 250, 252, 255))
    draw = ImageDraw.Draw(img)
    # Subtle studio background vignette / border
    draw.rounded_rectangle([4, 4, 396, 316], radius=12, fill=(248, 250, 252), outline=(226, 232, 240), width=1)
    # Floor contact shadow
    draw.ellipse([50, 260, 350, 292], fill=(218, 225, 235, 140))
    return img, draw

def save_image(img, filename):
    dest = os.path.join(IMAGES_DIR, filename)
    img.save(dest, 'PNG', optimize=True)
    print(f"[STUDIO RENDER] {filename} ({os.path.getsize(dest)} bytes)")

# -------------------------------------------------------------
# Studio Renderers by Category
# -------------------------------------------------------------

def render_laptop(filename, brand, model, accent_color=(56, 189, 248), body_color=(30, 41, 59), trackpoint=False):
    img, draw = create_studio_canvas()
    font_sm = get_font(12)
    font_bold = get_font(14)

    # Lid / Screen
    draw.rounded_rectangle([75, 45, 325, 205], radius=8, fill=body_color, outline=(71, 85, 105), width=2)
    # Display Bezel & Screen
    draw.rectangle([85, 55, 315, 195], fill=(15, 23, 42))
    # Glowing wallpaper gradient
    draw.rounded_rectangle([92, 62, 308, 188], radius=4, fill=accent_color)
    draw.rectangle([92, 140, 308, 188], fill=(15, 23, 42, 180)) # Taskbar & overlay
    
    # Camera notch / webcam
    draw.ellipse([197, 49, 203, 53], fill=(100, 116, 139))

    # Base & Keyboard deck (perspective trapezoid)
    draw.polygon([(50, 205), (350, 205), (370, 255), (30, 255)], fill=(148, 163, 184), outline=(100, 116, 139), width=2)
    # Keyboard island
    draw.polygon([(75, 210), (325, 210), (340, 235), (60, 235)], fill=(30, 41, 59))
    # Keycap grid lines
    for x in range(85, 320, 20):
        draw.line([(x, 212), (x - 3, 233)], fill=(51, 65, 85), width=1)
    
    # Red TrackPoint for ThinkPad
    if trackpoint:
        draw.ellipse([196, 220, 204, 226], fill=(220, 38, 38))

    # Trackpad
    draw.rectangle([165, 238, 235, 252], fill=(203, 213, 225), outline=(100, 116, 139))

    # Brand badge tag
    draw.rounded_rectangle([15, 15, 120, 36], radius=6, fill=(255, 255, 255, 230), outline=(203, 213, 225))
    draw.text((22, 18), brand, fill=(15, 23, 42), font=font_bold)
    save_image(img, filename)

def render_smartphone(filename, brand, model, accent_color=(59, 130, 246), frame_color=(30, 41, 59), camera_layout="triple"):
    img, draw = create_studio_canvas()
    font_bold = get_font(13)

    # Phone body (6.7" curved ratio)
    draw.rounded_rectangle([135, 35, 265, 265], radius=24, fill=frame_color, outline=(100, 116, 139), width=3)
    # Glass screen
    draw.rounded_rectangle([140, 40, 260, 260], radius=20, fill=(15, 23, 42))
    # Wallpaper gradient
    draw.rounded_rectangle([143, 43, 257, 257], radius=18, fill=accent_color)
    draw.rectangle([143, 150, 257, 257], fill=(15, 23, 42, 160)) # bottom dark gradient
    
    # Dynamic Island / Punch hole
    if camera_layout == "island":
        draw.rounded_rectangle([182, 48, 218, 56], radius=4, fill=(0, 0, 0))
    elif camera_layout == "visor": # Pixel visor
        draw.ellipse([198, 48, 202, 52], fill=(0, 0, 0))
        draw.rectangle([135, 75, 265, 95], fill=(30, 41, 59))
        draw.ellipse([155, 80, 175, 90], fill=(15, 23, 42))
        draw.ellipse([185, 81, 195, 89], fill=(15, 23, 42))
    elif camera_layout == "glyph": # Nothing Phone
        draw.ellipse([198, 48, 202, 52], fill=(0, 0, 0))
        # Glyph white illuminated strips on top right
        draw.arc([160, 70, 240, 150], 30, 160, fill=(255, 255, 255), width=3)
        draw.line([(200, 160), (200, 230)], fill=(255, 255, 255), width=3)
    else:
        draw.ellipse([198, 48, 202, 52], fill=(0, 0, 0))

    # Lock screen clock
    draw.text((170, 70), "09:41", fill=(255, 255, 255), font=font_bold)

    # Brand badge
    draw.rounded_rectangle([15, 15, 120, 36], radius=6, fill=(255, 255, 255, 230), outline=(203, 213, 225))
    draw.text((22, 18), brand, fill=(15, 23, 42), font=font_bold)
    save_image(img, filename)

def render_tablet(filename, brand, model, accent_color=(99, 102, 241), with_pen=True):
    img, draw = create_studio_canvas()
    font_bold = get_font(13)

    # Tablet body (11" / 12" wide ratio)
    draw.rounded_rectangle([75, 50, 305, 245], radius=16, fill=(30, 41, 59), outline=(100, 116, 139), width=2)
    # Screen
    draw.rounded_rectangle([82, 57, 298, 238], radius=12, fill=accent_color)
    draw.rectangle([82, 140, 298, 238], fill=(15, 23, 42, 140))
    # Front camera
    draw.ellipse([188, 52, 192, 56], fill=(148, 163, 184))

    # Stylus Pen (magnetically attached to right)
    if with_pen:
        draw.rounded_rectangle([315, 70, 323, 220], radius=4, fill=(241, 245, 249), outline=(148, 163, 184), width=1)
        # Pen tip
        draw.polygon([(315, 220), (323, 220), (319, 232)], fill=(15, 23, 42))

    # Brand badge
    draw.rounded_rectangle([15, 15, 120, 36], radius=6, fill=(255, 255, 255, 230), outline=(203, 213, 225))
    draw.text((22, 18), brand, fill=(15, 23, 42), font=font_bold)
    save_image(img, filename)

def render_watch(filename, brand, model, dial_shape="round", strap_color=(51, 65, 85), face_color=(15, 23, 42)):
    img, draw = create_studio_canvas()
    font_bold = get_font(13)

    # Top & Bottom Strap
    draw.rounded_rectangle([170, 25, 230, 110], radius=8, fill=strap_color)
    draw.rounded_rectangle([170, 190, 230, 275], radius=8, fill=strap_color)

    # Dial Casing
    if dial_shape == "round":
        draw.ellipse([140, 90, 260, 210], fill=(203, 213, 225), outline=(100, 116, 139), width=3)
        draw.ellipse([148, 98, 252, 202], fill=face_color)
        # Digital Crown
        draw.rounded_rectangle([258, 135, 266, 165], radius=3, fill=(148, 163, 184))
        # Watch Face Elements (Time + Rings)
        draw.text((176, 135), "10:08", fill=(255, 255, 255), font=font_bold)
        draw.arc([165, 115, 235, 185], 0, 240, fill=(16, 185, 129), width=3)
    else: # Rectangular
        draw.rounded_rectangle([140, 85, 260, 215], radius=24, fill=(203, 213, 225), outline=(100, 116, 139), width=3)
        draw.rounded_rectangle([147, 92, 253, 208], radius=18, fill=face_color)
        draw.rounded_rectangle([258, 135, 265, 165], radius=3, fill=(239, 68, 68)) # Action button
        draw.text((175, 135), "09:41", fill=(255, 255, 255), font=font_bold)
        draw.arc([160, 110, 240, 190], -30, 220, fill=(245, 158, 11), width=3)

    # Brand badge
    draw.rounded_rectangle([15, 15, 120, 36], radius=6, fill=(255, 255, 255, 230), outline=(203, 213, 225))
    draw.text((22, 18), brand, fill=(15, 23, 42), font=font_bold)
    save_image(img, filename)

def render_charger(filename, brand, model, watts="100W", body_color=(241, 245, 249), ports=3):
    img, draw = create_studio_canvas()
    font_bold = get_font(13)
    font_lg = get_font(16)

    # Charger Block (Isometric/Angled)
    draw.rounded_rectangle([115, 75, 265, 225], radius=16, fill=body_color, outline=(203, 213, 225), width=2)
    # Foldable prongs on back
    draw.rounded_rectangle([85, 115, 115, 135], radius=2, fill=(148, 163, 184))
    draw.rounded_rectangle([85, 165, 115, 185], radius=2, fill=(148, 163, 184))

    # Face plate
    draw.rounded_rectangle([200, 85, 255, 215], radius=10, fill=(30, 41, 59))
    # USB-C Ports (Orange/Blue accent)
    draw.rounded_rectangle([212, 100, 242, 112], radius=4, fill=(15, 23, 42), outline=(245, 158, 11), width=1.5)
    draw.rounded_rectangle([212, 125, 242, 137], radius=4, fill=(15, 23, 42), outline=(56, 189, 248), width=1.5)
    if ports >= 3:
        draw.rounded_rectangle([212, 150, 242, 162], radius=4, fill=(15, 23, 42), outline=(56, 189, 248), width=1.5)
    if ports >= 4:
        draw.rectangle([212, 175, 242, 192], fill=(15, 23, 42), outline=(16, 185, 129), width=1.5)

    # Wattage Text
    draw.text((125, 135), watts, fill=(15, 23, 42) if body_color[0] > 150 else (255, 255, 255), font=font_lg)
    draw.text((125, 160), "GaN Fast", fill=(100, 116, 139), font=font_bold)

    # Brand badge
    draw.rounded_rectangle([15, 15, 120, 36], radius=6, fill=(255, 255, 255, 230), outline=(203, 213, 225))
    draw.text((22, 18), brand, fill=(15, 23, 42), font=font_bold)
    save_image(img, filename)

def render_cable(filename, brand, model, cable_color=(30, 41, 59), connector_from="Type-C", connector_to="Type-C"):
    img, draw = create_studio_canvas()
    font_bold = get_font(13)

    # Coiled braided cable loop
    for r in range(55, 75, 4):
        draw.arc([130 - r, 120 - r, 170 + r, 180 + r], -40, 220, fill=cable_color, width=5)
    
    # Left Connector
    draw.rounded_rectangle([80, 140, 130, 170], radius=6, fill=(100, 116, 139), outline=(71, 85, 105))
    draw.rounded_rectangle([60, 148, 80, 162], radius=3, fill=(203, 213, 225)) # Metal tip
    
    # Right Connector
    draw.rounded_rectangle([270, 120, 320, 150], radius=6, fill=(100, 116, 139), outline=(71, 85, 105))
    draw.rounded_rectangle([320, 128, 340, 142], radius=3, fill=(203, 213, 225)) # Metal tip

    # Brand badge
    draw.rounded_rectangle([15, 15, 120, 36], radius=6, fill=(255, 255, 255, 230), outline=(203, 213, 225))
    draw.text((22, 18), brand, fill=(15, 23, 42), font=font_bold)
    save_image(img, filename)

def render_audio(filename, brand, model, audio_type="headphones", accent_color=(30, 41, 59)):
    img, draw = create_studio_canvas()
    font_bold = get_font(13)

    if audio_type == "headphones":
        # Headband Arch
        draw.arc([120, 50, 280, 220], 190, 350, fill=(71, 85, 105), width=10)
        draw.arc([125, 55, 275, 215], 195, 345, fill=(15, 23, 42), width=6) # Cushion
        # Left Ear Cup
        draw.rounded_rectangle([100, 140, 145, 225], radius=18, fill=accent_color, outline=(100, 116, 139), width=2)
        draw.ellipse([108, 155, 137, 210], fill=(15, 23, 42)) # Cushion ring
        # Right Ear Cup
        draw.rounded_rectangle([255, 140, 300, 225], radius=18, fill=accent_color, outline=(100, 116, 139), width=2)
        draw.ellipse([263, 155, 292, 210], fill=(15, 23, 42))
    elif audio_type == "earbuds":
        # Charging case
        draw.rounded_rectangle([130, 100, 270, 210], radius=24, fill=accent_color, outline=(100, 116, 139), width=2)
        # Seam line & LED indicator
        draw.line([(130, 150), (270, 150)], fill=(71, 85, 105), width=1)
        draw.ellipse([198, 165, 202, 169], fill=(16, 185, 129)) # green LED
        # Left & Right Earbuds protruding
        draw.rounded_rectangle([160, 70, 185, 115], radius=8, fill=(241, 245, 249), outline=(203, 213, 225))
        draw.rounded_rectangle([215, 70, 240, 115], radius=8, fill=(241, 245, 249), outline=(203, 213, 225))
    else: # Speaker
        draw.rounded_rectangle([120, 110, 280, 200], radius=20, fill=accent_color, outline=(100, 116, 139), width=2)
        # Speaker grille pattern
        for y in range(125, 185, 10):
            draw.line([(140, y), (260, y)], fill=(15, 23, 42), width=2)
        draw.ellipse([190, 145, 210, 165], fill=(239, 68, 68)) # Logo badge

    # Brand badge
    draw.rounded_rectangle([15, 15, 120, 36], radius=6, fill=(255, 255, 255, 230), outline=(203, 213, 225))
    draw.text((22, 18), brand, fill=(15, 23, 42), font=font_bold)
    save_image(img, filename)

def render_case(filename, brand, model, case_color=(15, 23, 42), has_magsafe=True, is_clear=False):
    img, draw = create_studio_canvas()
    font_bold = get_font(13)

    # Phone Case outline
    fill_col = (226, 232, 240, 180) if is_clear else case_color
    draw.rounded_rectangle([135, 45, 265, 265], radius=24, fill=fill_col, outline=(100, 116, 139), width=3)
    
    # Camera cutout (Top Left)
    draw.rounded_rectangle([145, 55, 195, 105], radius=10, fill=(30, 41, 59), outline=(71, 85, 105), width=2)
    draw.ellipse([152, 62, 168, 78], fill=(15, 23, 42))
    draw.ellipse([172, 62, 188, 78], fill=(15, 23, 42))
    draw.ellipse([152, 82, 168, 98], fill=(15, 23, 42))

    # MagSafe Ring
    if has_magsafe:
        draw.ellipse([160, 130, 240, 210], outline=(203, 213, 225), width=3)
        draw.line([(200, 212), (200, 230)], fill=(203, 213, 225), width=3)

    # Brand badge
    draw.rounded_rectangle([15, 15, 120, 36], radius=6, fill=(255, 255, 255, 230), outline=(203, 213, 225))
    draw.text((22, 18), brand, fill=(15, 23, 42), font=font_bold)
    save_image(img, filename)

def render_monitor(filename, brand, model, curved=False, accent_glow=(59, 130, 246)):
    img, draw = create_studio_canvas()
    font_bold = get_font(13)

    # Display Stand Base & Stem
    draw.polygon([(170, 220), (230, 220), (245, 265), (155, 265)], fill=(148, 163, 184), outline=(100, 116, 139))
    draw.rectangle([192, 170, 208, 230], fill=(100, 116, 139))

    # Monitor Panel
    if curved:
        # Arc screen contour
        draw.rounded_rectangle([50, 45, 350, 215], radius=12, fill=(15, 23, 42), outline=(71, 85, 105), width=2)
        draw.rounded_rectangle([58, 53, 342, 207], radius=8, fill=accent_glow)
        # Curve reflection lines
        draw.line([(65, 55), (100, 205)], fill=(255, 255, 255, 70), width=3)
    else:
        draw.rounded_rectangle([55, 45, 345, 215], radius=6, fill=(15, 23, 42), outline=(71, 85, 105), width=2)
        draw.rounded_rectangle([60, 50, 340, 205], radius=3, fill=accent_glow)

    # Screen graphic (vibrant game/code backdrop)
    draw.rectangle([60, 140, 340, 205], fill=(15, 23, 42, 160))

    # Brand badge
    draw.rounded_rectangle([15, 15, 120, 36], radius=6, fill=(255, 255, 255, 230), outline=(203, 213, 225))
    draw.text((22, 18), brand, fill=(15, 23, 42), font=font_bold)
    save_image(img, filename)

def render_smarthome(filename, brand, model, device_kind="cam"):
    img, draw = create_studio_canvas()
    font_bold = get_font(13)

    if device_kind == "cam": # Security Camera
        draw.rounded_rectangle([150, 160, 250, 230], radius=16, fill=(241, 245, 249), outline=(203, 213, 225), width=2)
        draw.ellipse([140, 80, 260, 200], fill=(255, 255, 255), outline=(203, 213, 225), width=2)
        draw.ellipse([165, 105, 235, 175], fill=(15, 23, 42)) # Lens dome
        draw.ellipse([185, 125, 215, 155], fill=(30, 58, 138)) # Glass lens
        draw.ellipse([197, 137, 203, 143], fill=(56, 189, 248)) # Lens glint
    elif device_kind == "speaker": # Smart Speaker Echo / Nest
        draw.ellipse([130, 90, 270, 230], fill=(71, 85, 105), outline=(51, 65, 85), width=2)
        draw.ellipse([135, 95, 265, 225], fill=(30, 41, 59))
        draw.ellipse([145, 105, 255, 215], fill=(51, 65, 85))
        draw.arc([132, 92, 268, 228], -30, 150, fill=(56, 189, 248), width=4) # Cyan LED ring
    elif device_kind == "bulb": # Smart bulb
        draw.ellipse([150, 70, 250, 170], fill=(254, 240, 138), outline=(250, 204, 21), width=2) # Glow
        draw.polygon([(170, 150), (230, 150), (220, 210), (180, 210)], fill=(241, 245, 249))
        draw.rectangle([185, 210, 215, 240], fill=(148, 163, 184)) # Screw base
    elif device_kind == "fan": # Ceiling Fan
        draw.ellipse([175, 135, 225, 185], fill=(255, 255, 255), outline=(203, 213, 225), width=2)
        draw.polygon([(200, 135), (140, 60), (160, 50)], fill=(241, 245, 249))
        draw.polygon([(200, 185), (260, 260), (240, 270)], fill=(241, 245, 249))
        draw.polygon([(225, 160), (320, 150), (320, 170)], fill=(241, 245, 249))
    elif device_kind == "purifier": # Air Purifier
        draw.rounded_rectangle([140, 60, 260, 250], radius=16, fill=(255, 255, 255), outline=(203, 213, 225), width=2)
        # OLED Circular screen
        draw.ellipse([180, 85, 220, 125], fill=(15, 23, 42))
        draw.text((188, 100), "012", fill=(16, 185, 129), font=font_bold)
        # Filter holes
        for y in range(150, 235, 8):
            for x in range(155, 245, 10):
                draw.ellipse([x, y, x + 3, y + 3], fill=(203, 213, 225))
    else: # Lock
        draw.rounded_rectangle([155, 50, 245, 260], radius=14, fill=(15, 23, 42), outline=(71, 85, 105), width=2)
        draw.ellipse([185, 80, 215, 110], fill=(30, 41, 59), outline=(56, 189, 248), width=2) # Fingerprint
        # Keypad digits
        for y in range(130, 180, 14):
            for x in range(175, 230, 20):
                draw.ellipse([x, y, x + 6, y + 6], fill=(148, 163, 184))
        draw.rounded_rectangle([175, 205, 225, 245], radius=6, fill=(71, 85, 105)) # Handle

    # Brand badge
    draw.rounded_rectangle([15, 15, 120, 36], radius=6, fill=(255, 255, 255, 230), outline=(203, 213, 225))
    draw.text((22, 18), brand, fill=(15, 23, 42), font=font_bold)
    save_image(img, filename)

def render_desksetup(filename, brand, model, device_kind="streamdeck"):
    img, draw = create_studio_canvas()
    font_bold = get_font(13)

    if device_kind == "streamdeck":
        draw.polygon([(90, 100), (310, 100), (330, 230), (70, 230)], fill=(30, 41, 59), outline=(71, 85, 105), width=2)
        # 15 Key Grid (3 rows x 5 cols)
        colors = [(239, 68, 68), (245, 158, 11), (16, 185, 129), (59, 130, 246), (139, 92, 246)]
        for r in range(3):
            for c in range(5):
                kx = 105 + c * 38
                ky = 120 + r * 32
                col = colors[(r + c) % len(colors)]
                draw.rounded_rectangle([kx, ky, kx + 28, ky + 24], radius=4, fill=(15, 23, 42), outline=col, width=2)
    elif device_kind == "mic": # Studio microphone
        draw.ellipse([150, 240, 250, 270], fill=(71, 85, 105)) # Heavy base
        draw.rectangle([195, 170, 205, 245], fill=(100, 116, 139)) # Stem
        # Shock mount ring
        draw.ellipse([155, 100, 245, 190], outline=(148, 163, 184), width=3)
        # Capsule body
        draw.rounded_rectangle([175, 70, 225, 180], radius=16, fill=(15, 23, 42), outline=(71, 85, 105), width=2)
        draw.rounded_rectangle([180, 75, 220, 120], radius=12, fill=(30, 41, 59)) # Grille
        for gy in range(80, 115, 4):
            draw.line([(182, gy), (218, gy)], fill=(100, 116, 139), width=1)
    elif device_kind == "webcam": # Webcam
        draw.rounded_rectangle([130, 110, 270, 180], radius=16, fill=(15, 23, 42), outline=(71, 85, 105), width=2)
        draw.ellipse([175, 120, 225, 170], fill=(30, 41, 59), outline=(56, 189, 248), width=2) # Glass lens
        draw.ellipse([192, 137, 208, 153], fill=(30, 58, 138))
        draw.ellipse([200, 142, 205, 147], fill=(255, 255, 255))
        # Monitor mount clamp
        draw.polygon([(185, 180), (215, 180), (220, 240), (180, 240)], fill=(71, 85, 105))
    elif device_kind == "light": # Key Light / ScreenBar
        draw.rounded_rectangle([80, 60, 320, 190], radius=8, fill=(255, 255, 255), outline=(203, 213, 225), width=2)
        draw.rectangle([88, 68, 312, 182], fill=(254, 249, 195)) # Warm white diffused glow
        draw.rectangle([195, 190, 205, 260], fill=(100, 116, 139)) # Pole
        draw.ellipse([160, 255, 240, 275], fill=(71, 85, 105))
    else:
        draw.rounded_rectangle([100, 100, 300, 200], radius=12, fill=(30, 41, 59))

    # Brand badge
    draw.rounded_rectangle([15, 15, 120, 36], radius=6, fill=(255, 255, 255, 230), outline=(203, 213, 225))
    draw.text((22, 18), brand, fill=(15, 23, 42), font=font_bold)
    save_image(img, filename)

def render_gaming(filename, brand, model, device_kind="controller"):
    img, draw = create_studio_canvas()
    font_bold = get_font(13)

    if device_kind == "controller": # DualSense / Xbox
        # Hand grips
        draw.polygon([(100, 95), (300, 95), (325, 240), (275, 245), (240, 160), (160, 160), (125, 245), (75, 240)], fill=(248, 250, 252), outline=(148, 163, 184), width=2)
        # Touchpad / Center
        draw.rounded_rectangle([155, 100, 245, 145], radius=6, fill=(30, 41, 59))
        draw.arc([150, 95, 250, 150], 0, 180, fill=(59, 130, 246), width=2) # Blue LED glow
        # Thumbsticks
        draw.ellipse([140, 160, 180, 200], fill=(51, 65, 85), outline=(15, 23, 42), width=2)
        draw.ellipse([220, 160, 260, 200], fill=(51, 65, 85), outline=(15, 23, 42), width=2)
        # D-Pad (Left)
        draw.rectangle([115, 125, 135, 145], fill=(71, 85, 105))
        # ABXY Buttons (Right)
        draw.ellipse([265, 115, 275, 125], fill=(239, 68, 68))
        draw.ellipse([275, 125, 285, 135], fill=(16, 185, 129))
        draw.ellipse([255, 125, 265, 135], fill=(59, 130, 246))
        draw.ellipse([265, 135, 275, 145], fill=(245, 158, 11))
    elif device_kind == "mouse": # Gaming Mouse
        draw.ellipse([140, 60, 260, 240], fill=(15, 23, 42), outline=(71, 85, 105), width=2)
        draw.line([(200, 60), (200, 130)], fill=(71, 85, 105), width=2) # Split buttons
        draw.rounded_rectangle([193, 80, 207, 110], radius=4, fill=(16, 185, 129)) # Scroll wheel RGB
        draw.arc([170, 170, 230, 220], 30, 150, fill=(16, 185, 129), width=3) # Logo glow
    elif device_kind == "keyboard": # Gaming Keyboard
        draw.rounded_rectangle([60, 90, 340, 220], radius=8, fill=(15, 23, 42), outline=(71, 85, 105), width=2)
        colors = [(239, 68, 68), (245, 158, 11), (16, 185, 129), (59, 130, 246), (168, 85, 247)]
        for r in range(4):
            for c in range(12):
                kx = 75 + c * 21
                ky = 105 + r * 24
                col = colors[(r + c) % len(colors)]
                draw.rectangle([kx, ky, kx + 16, ky + 18], fill=(30, 41, 59), outline=col, width=1.5)
    elif device_kind == "psu": # Power Supply
        draw.rounded_rectangle([120, 80, 280, 220], radius=10, fill=(15, 23, 42), outline=(71, 85, 105), width=2)
        draw.ellipse([150, 100, 250, 200], fill=(30, 41, 59), outline=(245, 158, 11), width=3) # Fan grill
        for a in range(0, 360, 45):
            draw.arc([160, 110, 240, 190], a, a + 25, fill=(71, 85, 105), width=3)
        draw.text((165, 145), "850W GOLD", fill=(245, 158, 11), font=font_bold)
    else: # Headset
        draw.arc([120, 50, 280, 220], 190, 350, fill=(239, 68, 68), width=8)
        draw.rounded_rectangle([100, 140, 145, 225], radius=18, fill=(15, 23, 42), outline=(239, 68, 68), width=2)
        draw.rounded_rectangle([255, 140, 300, 225], radius=18, fill=(15, 23, 42), outline=(239, 68, 68), width=2)
        # Boom mic
        draw.line([(120, 210), (150, 240), (190, 240)], fill=(148, 163, 184), width=3)
        draw.ellipse([185, 235, 198, 245], fill=(239, 68, 68))

    # Brand badge
    draw.rounded_rectangle([15, 15, 120, 36], radius=6, fill=(255, 255, 255, 230), outline=(203, 213, 225))
    draw.text((22, 18), brand, fill=(15, 23, 42), font=font_bold)
    save_image(img, filename)

def render_power(filename, brand, model, device_kind="ups"):
    img, draw = create_studio_canvas()
    font_bold = get_font(13)

    if device_kind == "ups": # Desktop Tower UPS
        draw.rounded_rectangle([135, 50, 265, 250], radius=10, fill=(15, 23, 42), outline=(71, 85, 105), width=2)
        # LCD Display
        draw.rounded_rectangle([155, 75, 245, 125], radius=6, fill=(8, 47, 73), outline=(14, 165, 233), width=1.5)
        draw.text((165, 85), "230V IN", fill=(56, 189, 248), font=font_bold)
        draw.text((165, 105), "100% BAT", fill=(16, 185, 129), font=font_bold)
        # Power button
        draw.ellipse([185, 145, 215, 175], fill=(30, 41, 59), outline=(16, 185, 129), width=2)
        # Ventilation slats
        for y in range(195, 235, 8):
            draw.line([(155, y), (245, y)], fill=(51, 65, 85), width=2)
    elif device_kind == "stabilizer": # Voltage Stabilizer
        draw.rounded_rectangle([125, 75, 275, 225], radius=12, fill=(241, 245, 249), outline=(203, 213, 225), width=2)
        # Seven-segment display
        draw.rounded_rectangle([165, 100, 235, 140], radius=6, fill=(15, 23, 42))
        draw.text((178, 110), "220", fill=(239, 68, 68), font=font_bold)
        draw.ellipse([190, 160, 210, 180], fill=(16, 185, 129)) # OK LED
    elif device_kind == "strip": # Power Strip Surge Protector
        draw.rounded_rectangle([80, 110, 320, 190], radius=10, fill=(248, 250, 252), outline=(203, 213, 225), width=2)
        # Red master switch
        draw.rectangle([100, 130, 125, 170], fill=(239, 68, 68))
        # 4 Universal Sockets
        for x in range(145, 300, 40):
            draw.ellipse([x, 135, x + 25, 165], fill=(226, 232, 240), outline=(148, 163, 184))
            draw.ellipse([x + 6, 142, x + 10, 146], fill=(15, 23, 42))
            draw.ellipse([x + 15, 142, x + 19, 146], fill=(15, 23, 42))
            draw.ellipse([x + 10, 152, x + 15, 157], fill=(15, 23, 42))
    else: # Wireless Pad
        draw.ellipse([130, 90, 270, 230], fill=(241, 245, 249), outline=(203, 213, 225), width=2)
        draw.ellipse([160, 120, 240, 200], outline=(148, 163, 184), width=3) # MagSafe ring
        draw.ellipse([195, 155, 205, 165], fill=(16, 185, 129))

    # Brand badge
    draw.rounded_rectangle([15, 15, 120, 36], radius=6, fill=(255, 255, 255, 230), outline=(203, 213, 225))
    draw.text((22, 18), brand, fill=(15, 23, 42), font=font_bold)
    save_image(img, filename)

print("Studio render engine loaded successfully.")
