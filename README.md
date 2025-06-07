# ApiWeather

<p align="center">

  ![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen)
![Maven](https://img.shields.io/badge/Maven-3.9.6-red)
  <img alt="GitHub language count" src="https://img.shields.io/github/languages/count/ArthurB95/ApiWeather?color=%2304D361">
  <img alt="Repository size" src="https://img.shields.io/github/repo-size/ArthurB95/ApiWeather">
  <a href="https://github.com/ArthurB95/ApiWeather/commits/master">
    <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/ArthurB95/ApiWeather">
  </a>
    
</p>

A Spring Boot application that generates through the zip code a response of data such as maximum and minimum temperature, location, longitude and latitude and many other information

## Table of Contents

- [About the project](#about-the-project)
- [How to Use](#how-to-use)
  - [Prerequisites](#prerequisites)
  - [Running the Application](#running-the-application)
    - [Local Development](#local-development)
- [API Endpoints](#api-endpoints)
- [License](#license)

## About the project

Develop API services capable of retrieving weather forecasts for a specific location. The application should:
1. Accept a zip code from users as input.
2. Provide the current temperature at the requested location as its primary output.
3. Offer additional details, such as the highest and lowest temperatures, and an extended forecast, as bonus features.
4. Implement caching to store forecast details for a duration of 15 minutes for subsequent requests using the same zip code.
5. Display an indicator to notify users if the result is retrieved from the cache.

## How to Use

This section provides comprehensive instructions for setting up and running the ApiWeather application.

### Prerequisites

- Java 21 JDK
- Maven

### Running the Application

#### Local Development

1. Build the project:
   ```bash
   mvn clean install
   ```

## API Endpoints

### GET /weather
 ```
http://localhost:8080/weather?zipCode=<zipCode>
   ```
**Response**

```json
{
	"zipCode": "60140061",
	"location": "60140-061, Centro, Fortaleza, Região Geográfica Imediata de Fortaleza, Região Geográfica Intermediária de Fortaleza, Ceará, Região Nordeste, Brasil",
	"latitude": -3.7345359,
	"longitude": -38.5205943,
	"currentTemperature": 26.5,
	"minTemperature": 24.6,
	"maxTemperature": 31.1,
	"forecast": [
		{
			"day": "2025-06-07",
			"min": 24.6,
			"max": 31.1
		},
		{
			"day": "2025-06-08",
			"min": 24.6,
			"max": 31.5
		},
		{
			"day": "2025-06-09",
			"min": 24.6,
			"max": 31.1
		},
		{
			"day": "2025-06-10",
			"min": 24.2,
			"max": 31.7
		},
		{
			"day": "2025-06-11",
			"min": 24.7,
			"max": 31.3
		},
		{
			"day": "2025-06-12",
			"min": 24.4,
			"max": 31.5
		},
		{
			"day": "2025-06-13",
			"min": 24.2,
			"max": 32.1
		}
	],
	"fromCache": false
}
```

**Error Response**

If an error occurs during ApiWeather, the API will return a 500 Internal Server Error.

**Example Usage**

```bash
curl --request GET \
  --url 'http://localhost:8080/weather?zipCode=60140061' \
  --header 'Content-Type: application/json'
```

## License

This project is licensed under the MIT License - see the LICENSE file for details. 
