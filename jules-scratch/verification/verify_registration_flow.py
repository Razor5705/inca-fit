from playwright.sync_api import sync_playwright, expect

def run_verification(playwright):
    browser = playwright.chromium.launch(headless=True)
    context = browser.new_context()
    page = context.new_page()

    try:
        # Step 1: Navigate to registration page
        page.goto("http://localhost:8080/registro")

        # Step 2: Fill out personal data (Paso 1)
        page.get_by_label("DNI").fill("12345678Z")
        page.get_by_label("Nombre Completo").fill("Jules Verne")
        page.get_by_label("Email").fill("jules.verne@example.com")
        page.get_by_label("Teléfono").fill("600112233")
        page.get_by_label("Contraseña").fill("password123")
        page.get_by_label("Confirmar Contraseña").fill("password123")
        page.get_by_role("button", name="Registrarse").click()

        # Step 3: Select membership (Paso 2)
        expect(page).to_have_url("http://localhost:8080/registro/paso2")
        page.get_by_role("button", name="Seleccionar").first.click()

        # Step 4: Simulate payment (Paso 3)
        expect(page).to_have_url("http://localhost:8080/registro/paso3")
        page.get_by_label("Nombre en la Tarjeta").fill("Jules N. Verne")
        page.get_by_label("Número de Tarjeta").fill("1111222233334444")
        page.get_by_label("Fecha de Caducidad (MM/YY)").fill("12/28")
        page.get_by_label("CVV").fill("123")
        page.get_by_role("button", name="Finalizar Registro").click()

        # Step 5: Verify successful registration on login page
        expect(page).to_have_url("http://localhost:8080/login?registroExitoso=true")
        success_message = page.locator(".alert-success")
        expect(success_message).to_be_visible()
        expect(success_message).to_have_text("¡Registro completado! Ahora puedes iniciar sesión.")
        page.screenshot(path="jules-scratch/verification/01_login_success.png")

        # Step 6: Log in with the new user
        page.get_by_label("Email").fill("jules.verne@example.com")
        page.get_by_label("Contraseña").fill("password123")
        page.get_by_role("button", name="Entrar").click()

        # Step 7: Verify dashboard
        expect(page).to_have_url("http://localhost:8080/dashboard")
        expect(page.get_by_text("Hola, Jules Verne!")).to_be_visible()
        expect(page.get_by_text("No tienes próximas reservas.")).to_be_visible()
        page.screenshot(path="jules-scratch/verification/02_dashboard_empty.png")

        print("Frontend verification script completed successfully.")

    except Exception as e:
        print(f"An error occurred: {e}")
        page.screenshot(path="jules-scratch/verification/error.png")
    finally:
        browser.close()

with sync_playwright() as playwright:
    run_verification(playwright)