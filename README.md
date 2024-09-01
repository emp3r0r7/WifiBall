# Real-Time 3D WiFi Data Visualization

This project visualizes real-time WiFi data in a 3D environment using [Babylon.js](https://www.babylonjs.com/) and a WebSocket connection to stream data from an Android sensor. The data includes access points detected by `airodump-ng`, their signal strength, and the current orientation of the Android device, which is used to position the access points in a 3D space.

![Project Overview](./images/overview.png)

## Features

- **Real-Time Data Visualization:** Access points are visualized in a 3D environment in real-time as they are detected.
- **Gyroscope Integration:** The position of access points is adjusted based on the current orientation of the device.
- **Dynamic Tooltip:** Hover over access points to see detailed information, including ESSID, BSSID, channel, signal strength, privacy, cipher, and authentication method.
- **Dynamic Coloring:** Access points are colored based on signal strength:
  - Blue: 0 to -45 dBm
  - Yellow: -45 to -70 dBm
  - Red: Below -70 dBm
- **Sidebar Toggle:** Easily toggle the visibility of a sidebar listing all detected access points.

## Installation

### Prerequisites

- [Node.js](https://nodejs.org/) (for running a local development server)
- [Java 8+](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html) (for backend services)
- [Android Studio](https://developer.android.com/studio) (for Android app development and testing)
- `airodump-ng` installed on your system.

### Setup

1. **Clone the Repository:**

    ```bash
    git clone https://github.com/emp3r0r7/WifiBall.git
    ```

2. **Install Dependencies:**

    ```bash
    sudo apt-get install aircrack-ng
    ```

3. **Serve the Application:**

    Serve the web application using a local development server:

    ```bash
    npm start
    ```

4. **Run the Backend:**

    Build and run the backend application to handle WebSocket connections and manage WiFi data:

    ```bash
    ./gradlew build
    java -jar build/libs/realtime-wifi-visualization.jar
    ```

5. **Configure the Android App:**

    - Connect your Android device and ensure it has the necessary sensors enabled.
    - Build and deploy the companion Android app from `android/` directory.

6. **Access the Application:**

    Open your web browser and navigate to `http://localhost:8011/` to see the real-time 3D visualization.

## Usage

- **Control the Camera:**
  - Use your mouse to rotate, zoom, and pan around the scene.
- **View Access Point Details:**
  - Hover over an access point to see detailed information about it in a tooltip.
- **Toggle Sidebar:**
  - Click the "Show AP List" button to toggle the visibility of the sidebar listing all detected access points.

## Project Structure

```bash
├── android/               # Android app source code
├── backend/               # Backend service source code
├── frontend/              # Frontend (Web) source code
│   ├── index.html         # Main HTML file
│   ├── css/               # Stylesheets
│   ├── js/                # JavaScript files (Babylon.js, etc.)
│   └── lib/               # External libraries (local copies)
├── images/                # Image assets for README and the app
└── README.md              # Project documentation
