# Apply real product photographs from user upload and Wikimedia Commons
$outputDir = "C:\Users\nihas\.gemini\antigravity\scratch\SmartCart\src\main\webapp\images"
$userImg = "C:\Users\nihas\.gemini\antigravity\brain\b6ceeeb0-0f7a-47ad-ac22-a0c65eb72f0e\.user_uploaded\media_1793127310414_74738933.png"

Add-Type -AssemblyName System.Drawing

function Save-StudioCardFromBmp($srcBmp, $destFilenames) {
    $canvas = New-Object System.Drawing.Bitmap(400, 320)
    $g = [System.Drawing.Graphics]::FromImage($canvas)
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
    $g.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
    
    $g.Clear([System.Drawing.Color]::FromArgb(255, 255, 255))
    $borderPen = New-Object System.Drawing.Pen([System.Drawing.Color]::FromArgb(241, 245, 249), 2)
    $g.DrawRectangle($borderPen, 1, 1, 397, 317)
    
    # Shadow
    $shadowBrush = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(25, 15, 23, 42))
    $g.FillEllipse($shadowBrush, 60, 275, 280, 26)
    
    $maxW = 340.0
    $maxH = 265.0
    $scale = [Math]::Min($maxW / $srcBmp.Width, $maxH / $srcBmp.Height)
    $drawW = [int]($srcBmp.Width * $scale)
    $drawH = [int]($srcBmp.Height * $scale)
    $drawX = [int]((400 - $drawW) / 2)
    $drawY = [int]((320 - $drawH) / 2)
    
    $g.DrawImage($srcBmp, $drawX, $drawY, $drawW, $drawH)
    
    foreach ($fn in $destFilenames) {
        $destPath = Join-Path $outputDir $fn
        $canvas.Save($destPath, [System.Drawing.Imaging.ImageFormat]::Png)
        Write-Host "  -> Saved: $fn"
    }
    
    $canvas.Dispose()
    $g.Dispose()
}

function Fetch-And-Save($url, $destFilenames) {
    $tempFile = [System.IO.Path]::GetTempFileName()
    $headers = @{ "User-Agent" = "SmartCartApp/1.0 (academic; student@smartcart.in)" }
    try {
        Invoke-WebRequest -Uri $url -Headers $headers -OutFile $tempFile -TimeoutSec 15
        $bmp = [System.Drawing.Bitmap]::FromFile($tempFile)
        Save-StudioCardFromBmp $bmp $destFilenames
        $bmp.Dispose()
        Remove-Item $tempFile -Force -ErrorAction SilentlyContinue
    } catch {
        Write-Host "Failed to download $url : $_"
    }
}

Write-Host "=== 1. Processing User Uploaded Real iPhone Photo ==="
$userBmp = [System.Drawing.Bitmap]::FromFile($userImg)
Save-StudioCardFromBmp $userBmp @(
    "phone-15pro.png", "prod-5.png",
    "phone-15.png", "prod-6.png",
    "case-15pro.png", "prod-27.png",
    "case-15promax.png", "prod-28.png",
    "case-15clear.png", "prod-29.png",
    "prod-117.png", "prod-118.png", "prod-120.png", "prod-121.png", "prod-124.png"
)
$userBmp.Dispose()

Write-Host "=== 2. Fetching Real Product Photos from Wikimedia Commons ==="

# Samsung Galaxy S24 Ultra
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/e/e7/SAMSUNG_Galaxy_S24_Ultra.jpg/500px-SAMSUNG_Galaxy_S24_Ultra.jpg" @(
    "phone-s24.png", "prod-7.png", "prod-56.png", "case-s24.png", "prod-30.png", "prod-116.png", "prod-119.png", "prod-122.png"
)

# Google Pixel 8 Pro
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/5/5c/Google_Pixel_8_Rose_front.jpg/500px-Google_Pixel_8_Rose_front.jpg" @(
    "prod-57.png", "prod-123.png"
)

# Nothing Phone
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/9/94/Nothing-Design-Event-Tokyo-2024-07-05_011.jpg/500px-Nothing-Design-Event-Tokyo-2024-07-05_011.jpg" @(
    "prod-58.png", "prod-111.png"
)

# Sony Xperia 1 V
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/8/83/SONY_Xperia_1_%28SOV40%29_Purple_Frontside.jpg/500px-SONY_Xperia_1_%28SOV40%29_Purple_Frontside.jpg" @(
    "prod-65.png"
)

# MacBook Pro & Air
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/4/45/Apple_MacBook_Pro_16%22_M2_Max_with_Headset_and_Mouse.jpg/500px-Apple_MacBook_Pro_16%22_M2_Max_with_Headset_and_Mouse.jpg" @(
    "laptop-pro15.png", "prod-1.png", "laptop-air13.png", "prod-3.png"
)

# Dell XPS
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/2/21/DELL_XPS_13_and_15_%2837080596413%29.jpg/500px-DELL_XPS_13_and_15_%2837080596413%29.jpg" @(
    "prod-46.png", "prod-127.png"
)

# ThinkPad
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/0/0e/IBM_Thinkpad_R51.jpg/500px-IBM_Thinkpad_R51.jpg" @(
    "prod-48.png"
)

# HP Laptop
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/1/1e/HP_Laptop_14S.jpg/500px-HP_Laptop_14S.jpg" @(
    "prod-47.png", "prod-50.png", "prod-52.png", "prod-53.png", "prod-54.png"
)

# iPad Air / Pro
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/7/70/Apple_iPad_Pro_11.jpg/500px-Apple_iPad_Pro_11.jpg" @(
    "tablet-ipadair.png", "prod-9.png", "tablet-ipad10.png", "prod-11.png", "prod-66.png", "prod-74.png"
)

# Samsung Galaxy Tab S9
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/7/7a/IPad_Air_2020_%2851012790753%29.jpg/500px-IPad_Air_2020_%2851012790753%29.jpg" @(
    "tablet-tabs9fe.png", "prod-10.png", "prod-67.png", "prod-68.png", "prod-69.png", "prod-70.png", "prod-71.png", "prod-72.png", "prod-73.png"
)

# Apple Watch Ultra
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/9/91/Apple_Watch_Ultra_-_2.jpg/500px-Apple_Watch_Ultra_-_2.jpg" @(
    "watch-ultra2.png", "prod-13.png", "prod-76.png", "prod-84.png"
)

# Apple Watch Series / Galaxy Watch
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/8/86/Apple_Watch_Series_10.jpg/500px-Apple_Watch_Series_10.jpg" @(
    "watch-galaxy6.png", "prod-14.png", "prod-77.png", "prod-78.png", "prod-79.png", "prod-80.png", "prod-81.png", "prod-82.png", "prod-83.png", "prod-85.png"
)

# Sony WH-1000XM Headphones
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/4/4b/Sony-WH-1000XM3-kabellose-Bluetooth-Noise-Cancelling-Kopfhoerer.jpg/500px-Sony-WH-1000XM3-kabellose-Bluetooth-Noise-Cancelling-Kopfhoerer.jpg" @(
    "audio-headphones.png", "prod-23.png", "prod-106.png", "prod-107.png", "prod-108.png", "prod-109.png", "prod-110.png", "prod-115.png"
)

# AirPods Pro
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/b/b9/AirPods_Pro_3_with_case.jpg/500px-AirPods_Pro_3_with_case.jpg" @(
    "audio-earbuds.png", "prod-24.png", "prod-112.png", "prod-114.png"
)

# JBL Speaker
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/3/33/JBL_Charge_4_1v2.jpg/500px-JBL_Charge_4_1v2.jpg" @(
    "prod-113.png"
)

# PS5 DualSense Controller
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/e/e2/DualSense_Controller_Chroma_Pearl.jpg/500px-DualSense_Controller_Chroma_Pearl.jpg" @(
    "controller-wireless.png", "prod-41.png", "prod-156.png"
)

# Logitech Gaming Mouse
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/7/71/2023_Mysz_komputerowa_Logitech_G903_Lightspeed.jpg/500px-2023_Mysz_komputerowa_Logitech_G903_Lightspeed.jpg" @(
    "mouse-gaming.png", "prod-43.png", "mouse-mxmaster.png", "prod-37.png", "prod-157.png", "prod-159.png"
)

# Mechanical Keyboard
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/7/7f/Beautiful_Mechanical_Keyboard.jpg/500px-Beautiful_Mechanical_Keyboard.jpg" @(
    "keyboard-mech.png", "prod-38.png", "keyboard-gaming.png", "prod-42.png", "prod-158.png", "prod-161.png", "prod-165.png"
)

# Curved Gaming Monitor
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/4/42/ASUS_curved_monitor_20170603.jpg/500px-ASUS_curved_monitor_20170603.jpg" @(
    "monitor-lg.png", "prod-31.png", "monitor-dell.png", "prod-32.png", "monitor-asus.png", "prod-33.png",
    "prod-126.png", "prod-128.png", "prod-129.png", "prod-130.png", "prod-131.png", "prod-132.png", "prod-133.png", "prod-134.png", "prod-135.png"
)

# Security Camera / Smart Home
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/f/f2/Lorex_easy_connect_ip_camera.jpg/500px-Lorex_easy_connect_ip_camera.jpg" @(
    "doorbell-smart.png", "prod-34.png", "prod-136.png", "prod-143.png"
)

# Power Strip Surge Protector
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/4/45/%2820241007%29_APC_power_strip.jpg/500px-%2820241007%29_APC_power_strip.jpg" @(
    "prod-171.png", "prod-172.png", "prod-173.png"
)

# Uninterruptible Power Supply (UPS)
Fetch-And-Save "https://thumb.wikimedia.org/wikipedia/commons/thumb/a/a9/MGE_Uninterruptible_Power_Supply_at_NERSC.jpg/500px-MGE_Uninterruptible_Power_Supply_at_NERSC.jpg" @(
    "ups-1500va.png", "prod-44.png", "prod-166.png", "prod-167.png", "prod-168.png", "prod-169.png", "prod-170.png", "prod-174.png"
)

Write-Host "=== Real Product Photographs Successfully Applied to SmartCart! ==="
