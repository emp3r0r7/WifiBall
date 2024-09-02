![Project Logo](https://emp3r0r7.neocities.org/images/wifiball/wifiball_logo1.jpg)
# WifiBall : Real-Time 3D WiFi Data Visualization

This project visualizes real-time WiFi data in a 3D environment using [Babylon.js](https://www.babylonjs.com/) and a WebSocket connection to stream data from an Android device gyroscope information. The data includes access points detected by `airodump-ng`, their signal strength, and the current orientation of the Android device, which is used to position the access points in a 3D space.

![Project Overview](https://emp3r0r7.neocities.org/images/wifiball/wifimap_4.jpg)

## Features

- **Real-Time Data Visualization:** Access points are visualized in a 3D environment in real-time as they are detected.
- **Gyroscope Integration:** The position of access points is adjusted based on the current orientation of the device.
- **Dynamic Tooltip:** Hover over access points to see detailed information, including ESSID, BSSID, channel, signal strength, privacy, cipher, and authentication method.
- **Dynamic Coloring:** Access points are colored based on signal strength:
  - Blue: 0 to -45 dBm
  - Yellow: -45 to -70 dBm
  - Red: Below -70 dBm
- **Sidebar Toggle:** Easily toggle the visibility of a sidebar listing all detected access points.
- **Associated Stations:** You can see each associated station to the selected access point.


## Prerequisites

- **Hardware:**
  - A directional WiFi antenna for accurate signal detection.
  - A WiFi card capable of monitor mode eg. [Alfa AWUS036NHA](https://www.alfa.com.tw/products/awus036nha?variant=36473966166088)
  - An Android device with a gyroscope sensor, mounted on the antenna to track its orientation.
  - A tripod to stabilize both the antenna and the Android device during the scan.

- **Software:**
  - Linux environment (I developed it on Debian)
  - [Java 21+](https://www.openlogic.com/openjdk-downloads?field_java_parent_version_target_id=828&field_operating_system_target_id=426&field_architecture_target_id=All&field_java_package_target_id=All) the application is a SpringBoot app relying on Java 21
  - `aircrack-ng` suite installed on your system.

### Setup Photos

To better understand the physical setup required for this project, refer to the following images:

![WiFiBall Setup 1](https://emp3r0r7.neocities.org/images/wifiball/wifimap_1.jpg)
*WiFiBall Setup with directional antenna and Android device mounted on a tripod.*

![WiFiBall Setup 2](https://emp3r0r7.neocities.org/images/wifiball/wifimap_2.jpg)
*Close-up of the Android device mounted on the antenna.*

## Installation

1. **Clone the Repository:**

    ```bash
    git clone https://github.com/emp3r0r7/WifiBall.git
    ```

2. **Install Dependencies:**

    ```bash
    sudo apt install aircrack-ng
    ```

3. **Run the Application:**

    Build and run the application to handle WebSocket connections and manage WiFi data:

    ```bash
    cd wifiball 
    ./mvnw clean package
    cp /target/wifiball.jar /your/desired/location
    java -jar /your/desired/location/wifiball.jar
    ```
    
4. **Connect your Android Device**

    - Launch the [Android App](https://github.com/emp3r0r7/GyroScope)
    - Connect your Android Device in the same network of your computer
    - Input your computer lan ip and WifiBall default port 8011

5. **Configure WifiBall**

    - Set your network interface card capable of monitor mode 

     ```bash
    cd ~
    nano .wifiball/config.properties
    
    ```
    Set your network card accordingly at network.card, always put the card managed state eg. wlan0 instead of wlan0mon

6. **Access the Application:**

    Open your web browser and navigate to `http://localhost:8011/` to see the real-time 3D visualization.

## Usage

- **Be smooth!:**
  - While turning your antenna and gyroscope around , be gentle , steady and slow , the application currently has a polling rate of 1 second, its useless to turn the antenna rapidly.
  - In order to have precise and accurate readings , you must scan the environment a lot, because higher signals will be shown by replacing weaker ones. You got the idea.
- **Control the Camera:**
  - Use your mouse to rotate, zoom, and pan around the scene.
- **View Access Point Details:**
  - Hover over an access point to see detailed information about it in a tooltip.
- **Toggle Sidebar:**
  - Click the "Show AP List" button to toggle the visibility of the sidebar listing all detected access points and their associated stations.

---
