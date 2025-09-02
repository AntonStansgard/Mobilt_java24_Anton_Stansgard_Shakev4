
# Mobilt_java24_Anton_Stansgard_Shakev4


## Beskrivning av projektet
Mitt projekt är en Android-app som använder enhetens accelerometer för att känna av rörelser och skakningar. Genom tre olika
Användaren kan slå på/av sensorn, justera känslighetsnivån (threshold), och se realtidsvärden för X, Y och Z-axlarna.


## Appens funktioner
- Starta/stoppa sensorn via en **switch**.
- Justera **threshold** med en **SeekBar** för att ändra känsligheten för skakdetektering.
- Visa aktuella sensorvärden (X, Y, Z).
- **Reset-knapp** för att återställa threshold till standardvärdet (2.5 g).
- Notifiering (Toast) visas när en skakning upptäcks.


## Teknologier
- **Språk:** Java
- **IDE:** Android Studio
- **Framework:** Android SDK
- **Sensorer:** Accelerometer via `SensorManager`


## Struktur
- `MainActivity.java` – Huvudlogiken för sensorn och UI-hantering.
- `activity_main.xml` – Layout för appen med switch, seekbar, knappar och textfält.


## För att köra appen
1. Klona projektet:
git clone https://github.com/Anton_Stansgard/Mobilt_java24_Anton_Stansgard_Shakev4.git

2. Öppna projektet i Android Studio.


3.Kör appen på en emulator eller fysisk enhet.


- I emulatorn: öppna Extended controls → Sensors för att simulera rörelse/acceleration.


- På fysisk enhet: skaka telefonen för att testa på riktigt.

 

