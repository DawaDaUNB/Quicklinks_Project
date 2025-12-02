# QuickLinks — Team 14 Final Project (CS2063, Fall 2025)

Team Members: Yohanne Noume, Dawa Penjor, Gabby Bryne

University of New Brunswick — Faculty of Computer Science

# Overview

QuickLinks is a native Android app that simplifies managing URLs and QR codes. It allows
users to shorten long URLs, generate QR codes, scan codes with the device's camera, and
maintain a searchable history of saved links. The app is designed for advertisers and
professionals who need clean, shareable links and quick access to previously used
resources.

- The app works fully offline using SQLite and offers guest mode for quick testing.

# Core Features

- **URL Shortener:** Converts long URLs into compact short links with copy, save, and
    open options.
- **QR Code Generator:** Generates customizable QR codes with RGB sliders and
    border size control.
- **QR Scanner:** Uses the camera to scan QR codes and saves them to history with a
    cooldown timer.
- **History Manager:** Stores all scanned/generated links locally. Features include:
    o Alphabetical sorting

```
o Collapsible views (shortcode, original URL, and title)
```
```
o Favoriting links (favorites appear at the top)
```
```
o Deletion confirmation with toast notification
```

- **Clipboard Detection:** Detects copied URLs and offers to paste or open them in-
    app.
- **Guest Mode:** Allows users to test all features without signing in.
- **Light/Dark Mode:** Automatically adapts to system theme.

# Source Code Access

A link to our GitHub repository is provided DawaDaUNB/Quicklinks_Project: Kotlin QR app,
and also a zipped source code is included in the submission. No API keys are included and
APK file is provided for testing.

NB: FallbackToDestructiveMigration() is just for our database testing purpose.

# Authentication & Account Setup

- Users can create a new account to access full features.
- Email is not the primary key instead, a Long ID is used for better security.
- Password reset: If the email exists in the database, the user can reset their
    password (no email verification required).
- Guest Mode is available for testing and demonstration. This allows full access to
    app features without signing in.

# Screens & Navigation

- **Welcome/Login/Signup:** Entry point with guest mode and account creation.
- **Home Dashboard:** Central hub with 4 main actions: Scan QR, Shorten URL,
    Generate QR, View History.


- **ShortenerScreen:** Input field for long URLs, generates short links with
    copy/save/open options.
- **QrGeneratorScreen:** Input field + sliders for RGB customization and border size.
    Preview pinned at the top.
- **ScannerScreen:** Live camera feed with auto-scan and cooldown timer. Successful
    scans redirect to History.
- **HistoryScreen:** List of saved links with copy/delete/favorite options. Includes
    search, alphabetical sorting, and collapsible views.
- **ForgotPasswordScreen:** Allows users to reset their password if their email exists in
    the database.

# Design Decisions & Feedback Integration

- **Simplified Scope:** Removed folder system and shared database in favor of SQLite
    for offline use.
- **UI Improvements:** Enlarged buttons, added icons, standardized layout, and added
    light/dark mode.
- **Clipboard Awareness:** Detects copied URLs and offers contextual actions.
- **Scanner Cooldown:** Prevents duplicate scans by disabling scanner briefly after
    success.
- **History Enhancements** : Alphabetical sorting, collapsible views, favoriting, and
    deletion confirmation.
- **Security Update:** Switched user primary key from email to Long ID.

# How to Use the App

1. Launch the app: Start at splash screen, then choose login or guest mode.
2. Try Guest Mode: No account needed; test all features freely.


3. Copy a URL: App detects clipboard content and offers to paste or open.
4. Shorten a URL: Paste a long URL and press "Shorten".
5. Generate a QR Code: Customize colors and borders, then save or share.
6. Scan a QR Code: Open Scanner tab, point camera, wait for detection.
7. View History: Access saved links, search, favorite, collapse, or delete.
8. Reset Password: If email exists in the database, reset password directly.

# Testing Notes

- Sign Up/Login requires creating a new account and remembering credentials.
- Guest mode allows full feature access without login.
- History page supports search, favorites, and collapsible views.
- Dark mode can be toggled via system settings.
- Back button on the Home screen prompts a logout confirmation dialog before
    returning to the Login page.

# Testing Case

**1. Create an account and login (Optional)**
    ➢ Go to the sign-up page, create your account then use your sign in credentials to
       login
**2. Use Guest Mode**
    ➢ Access all features without login
3. **Copy a URL before launching the app**
    ➢ App detects clipboard and offers to paste/open
**4. Shorten a long URL**
    ➢ Verify short link is generated and saved
**5. Generate a QR code**
    ➢ Customize colors and border, then save
**6. Scan a QR code**
    ➢ Verify scan is saved to history
**7. View History**


```
➢ Search, favorite, collapse, and delete entries
```
**8. Reset password**
    ➢ Enter existing email and confirm reset
**9. Toggle dark mode**
    ➢ Switch system theme and verify UI adapts
**10. Press back on the Home screen**
    ➢ Confirm logout dialog appears

# Release Notes

## Completed Features

- URL shortening
- QR code generation with customization
- QR scanning with cooldown
- History page with search, favorites, collapsible views
- Clipboard detection
- Guest mode
- Light/dark mode
- Password reset (no email verification)

## Incomplete / Deferred Features

- External app linking (e.g., Facebook, Instagram)
- Folder system for organizing links
- Email verification for password reset

# Supported API Levels

- Minimum API Level: 2 8 (Android 7.0)
- Target API Level: 34 (Android 14)
- The app works on Google Pixel 6 running Api level 36 (Android 16), Google Pixel 4a
    running API level 34 (Android 14), or Moto G7 running API level 30 (Android 10)
