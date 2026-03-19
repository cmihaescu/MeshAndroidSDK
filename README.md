# Payment App (Android template)

A minimal Android payment app template with a **Products** page (3 products) and a **Cart** page. No checkout flow.

## Features

- **Products screen**: Lists 3 sample products with name, description, price and “Add to cart”.
- **Cart screen**: Shows cart items, quantity, line subtotals, and total subtotal. Remove items or go back to shop.
- **Cart badge**: Toolbar shows cart icon with item count; tap to open cart.

## Tech stack

- Kotlin, Jetpack Compose, Material 3
- Navigation Compose, ViewModel, StateFlow for cart state
- Min SDK 26, target/compile SDK 34

## How to run

1. Open the project in Android Studio (e.g. **File → Open** and select this folder).
2. Sync Gradle and wait for dependencies to resolve.
3. Run on an emulator or device (**Run → Run 'app'** or the green play button).

If you use the command line and have the Gradle wrapper:

```bash
./gradlew installDebug
```

(On first run you may need to generate the wrapper with **File → New → New Project**, then copy the `gradle/wrapper` folder, or run `gradle wrapper` if you have Gradle installed.)

## Project structure

- `app/src/main/java/com/paymentapp/template/`
  - `MainActivity.kt` – Hosts Compose and navigation.
  - `cart/` – `CartState`, `CartViewModel`.
  - `data/` – `Product`, `CartItem`, `ProductsRepository` (3 products).
  - `ui/theme/` – Theme and colors.
  - `ui/products/ProductsScreen.kt` – Products list and “Add to cart”.
  - `ui/cart/CartScreen.kt` – Cart list, subtotal, remove, “Continue shopping”.

No checkout screen or payment logic is included; you can add them later.
