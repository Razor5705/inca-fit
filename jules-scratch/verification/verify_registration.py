from playwright.sync_api import sync_playwright, expect

def run(playwright):
    browser = playwright.chromium.launch(headless=True)
    context = browser.new_context()
    page = context.new_page()

    # Go to the home page first
    page.goto("http://localhost:8080/")

    # Click the registration button
    register_button = page.get_by_role("link", name="Registrarse")
    register_button.click()

    # Wait for the registration page to load and verify the URL and title
    expect(page).to_have_url("http://localhost:8080/registro")
    expect(page).to_have_title("Inca Fit")

    # Take the screenshot
    page.screenshot(path="jules-scratch/verification/registration.png")

    browser.close()

with sync_playwright() as playwright:
    run(playwright)