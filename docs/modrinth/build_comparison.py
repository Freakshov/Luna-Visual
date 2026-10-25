from pathlib import Path

from PIL import Image, ImageDraw, ImageFont, ImageFilter


ROOT = Path(__file__).resolve().parent
SOURCES = ROOT / "sources"
WIDTH = 2400
HEIGHT = 1740
FONT_REGULAR = Path("C:/Windows/Fonts/segoeui.ttf")
FONT_BOLD = Path("C:/Windows/Fonts/segoeuib.ttf")


def font(size, bold=False):
    return ImageFont.truetype(str(FONT_BOLD if bold else FONT_REGULAR), size)


def gradient(canvas):
    draw = ImageDraw.Draw(canvas)
    top = (8, 19, 34)
    bottom = (12, 31, 52)
    for y in range(HEIGHT):
        fraction = y / (HEIGHT - 1)
        color = tuple(round(top[channel] * (1 - fraction) + bottom[channel] * fraction) for channel in range(3))
        draw.line((0, y, WIDTH, y), fill=color)

    glow = Image.new("RGBA", canvas.size, (0, 0, 0, 0))
    glow_draw = ImageDraw.Draw(glow)
    glow_draw.ellipse((-400, -540, 980, 840), fill=(39, 175, 200, 38))
    glow_draw.ellipse((1530, 920, 2830, 2220), fill=(93, 113, 212, 28))
    canvas.paste(Image.alpha_composite(canvas.convert("RGBA"), glow.filter(ImageFilter.GaussianBlur(170))).convert("RGB"))


def rounded_card(draw, x, y, w, h, radius=28):
    draw.rounded_rectangle((x + 7, y + 10, x + w + 7, y + h + 10), radius=radius, fill="#06111f")
    draw.rounded_rectangle((x, y, x + w, y + h), radius=radius, fill="#13243a", outline="#344f69", width=2)


def screenshot(canvas, image_path, x, y, w, h, label, accent):
    original = Image.open(image_path).convert("RGB")
    if original.size != (1600, 900):
        raise ValueError(f"Unexpected screenshot dimensions: {image_path}: {original.size}")
    scaled = original.resize((w, h), Image.Resampling.LANCZOS)
    mask = Image.new("L", (w, h), 0)
    ImageDraw.Draw(mask).rounded_rectangle((0, 0, w - 1, h - 1), radius=16, fill=255)
    canvas.paste(scaled, (x, y), mask)
    draw = ImageDraw.Draw(canvas, "RGBA")
    draw.rounded_rectangle((x, y, x + w, y + h), radius=16, outline=(110, 144, 175, 190), width=2)

    label_font = font(24 if w > 700 else 18, bold=True)
    label_bounds = draw.textbbox((0, 0), label, font=label_font)
    label_width = label_bounds[2] - label_bounds[0] + 28
    label_height = 41 if w > 700 else 32
    draw.rounded_rectangle((x + 15, y + 15, x + 15 + label_width, y + 15 + label_height),
                           radius=10, fill=(6, 16, 29, 230), outline=accent, width=2)
    draw.text((x + 29, y + 17 if w > 700 else y + 18), label, font=label_font, fill=accent)


def comparison(canvas, x, y, w, h, title, before, after, featured=False):
    draw = ImageDraw.Draw(canvas)
    rounded_card(draw, x, y, w, h, 32 if featured else 25)
    accent = "#52c5d8"
    draw.rounded_rectangle((x + 25, y + 20, x + 34, y + (69 if featured else 43)), radius=4, fill=accent)
    draw.text((x + 51, y + (16 if featured else 13)), title,
              font=font(39 if featured else 28, bold=True), fill="#f4f8ff")

    inner_margin = 38 if featured else 44
    gap = 20 if featured else 16
    image_w = (w - inner_margin * 2 - gap) // 2
    image_h = round(image_w * 9 / 16)
    image_y = y + (86 if featured else 59)
    image_x = x + inner_margin
    screenshot(canvas, SOURCES / before, image_x, image_y, image_w, image_h,
               "ÖNCE / BEFORE", (255, 168, 147, 255))
    screenshot(canvas, SOURCES / after, image_x + image_w + gap, image_y, image_w, image_h,
               "SONRA / AFTER", (106, 225, 235, 255))


def build():
    canvas = Image.new("RGB", (WIDTH, HEIGHT), "#071320")
    gradient(canvas)
    draw = ImageDraw.Draw(canvas)

    draw.rounded_rectangle((66, 48, 75, 144), radius=4, fill="#56c7d6")
    draw.text((106, 45), "LUNA VISUAL", font=font(79, bold=True), fill="#f6f9ff")
    draw.text((112, 142), "GERÇEK OYUN GÖRÜNTÜLERİ  •  ÖNCE / SONRA", font=font(28), fill="#aac2d7")
    draw.rounded_rectangle((1883, 76, 2336, 137), radius=20, fill="#173750", outline="#397990", width=2)
    draw.text((1915, 89), "MINECRAFT 1.21.11", font=font(26, bold=True), fill="#d9f6fa")

    comparison(canvas, 60, 184, 2280, 714, "TAM PARLAKLIK  /  FULLBRIGHT",
               "fullbright-before.png", "fullbright-after.png", featured=True)
    comparison(canvas, 60, 928, 1128, 360, "WARDEN KARANLIĞI",
               "warden-before.png", "warden-after.png")
    comparison(canvas, 1212, 928, 1128, 360, "LAVDA NET GÖRÜŞ",
               "lava-before.png", "lava-after.png")
    comparison(canvas, 60, 1314, 1128, 360, "TOZ KARI SİSİ",
               "snow-before.png", "snow-after.png")
    comparison(canvas, 1212, 1314, 1128, 360, "SU SİSİ",
               "water-before.png", "water-after.png")

    draw = ImageDraw.Draw(canvas)
    draw.text((68, 1697), "DÖRT AYAR  •  BEŞ GÖRSEL KARŞILAŞTIRMA", font=font(20, bold=True), fill="#8aaac2")
    footer = "LUNAVISUAL"
    footer_font = font(20, bold=True)
    footer_box = draw.textbbox((0, 0), footer, font=footer_font)
    draw.text((2333 - (footer_box[2] - footer_box[0]), 1697), footer, font=footer_font, fill="#70d7e5")

    png_path = ROOT / "luna-visual-before-after.png"
    webp_path = ROOT / "luna-visual-before-after.webp"
    canvas.save(png_path, format="PNG", optimize=True)
    canvas.save(webp_path, format="WEBP", quality=90, method=6)
    for path in (png_path, webp_path):
        print(f"{path.name}: {path.stat().st_size / 1048576:.2f} MiB")


if __name__ == "__main__":
    build()
