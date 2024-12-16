# Weather API

## Project Usage Instructions

### 1. Project Overview

The `Weather API` project provides a weather query service that fetches weather data for a specific location via the Visual Crossing Weather API. It supports querying the weather for a specific day or a date range, providing temperature data in Celsius.

### 2. Environment Requirements

- **Java Version**: 1.8
- **Spring Boot Version**: 2.x
- **Dependency Management Tool**: Maven
- **Caching**: This project uses Spring’s caching mechanism with the `@Cacheable` annotation for cache management, with in-memory caching by default.
- **External API**: Uses `Visual Crossing Weather API` to get weather data. An API key (`API_KEY`) is required, which can be obtained by registering on their platform.

### 3. Configuration Instructions

Before starting the project, ensure the following configurations are included in the configuration file:

#### 3.1. `application.properties` Configuration

```properties
# Replace with your API key obtained from Visual Crossing Weather API
WEATHER_API_KEY=your_api_key
```

### 4. How to Deploy

1. **Clone the Project**: You can clone the project repository via Git:

   ```bash
   git clone https://your-repository-url
   ```

2. **Build the Project**: Build the project using Maven:

   ```bash
   mvn clean install
   ```

3. **Run the Project**: Run the Spring Boot project using Maven:

   ```bash
   mvn spring-boot:run
   ```

4. **Project Running**: Once the project is running, you can access the following endpoints via the browser to query weather data:

   - **Single Location Weather Query**:

     ```http
     GET http://localhost:8080/{address}
     ```

     Example:

     ```http
     GET http://localhost:8080/beijing
     ```

     This endpoint returns weather data for the specified location for the current day.

   - **Date Range Weather Query**:

     ```http
     GET http://localhost:8080/{address}/{startTime}/{endTime}
     ```

     Example:

     ```http
     GET http://localhost:8080/beijing/2024-12-16/2024-12-18
     ```

     This endpoint returns weather data for the specified date range.

   - **14-Day Weather Query**:

     ```http
     GET http://localhost:8080/{address}/all
     ```

     Example:

     ```http
     GET http://localhost:8080/beijing/all
     ```

     This endpoint returns weather data for the specified location, covering the day before and the following 13 days.

### 5. Features

This project provides the following main features:

1. **Get Current Day Weather for a Specific Location**:
   - **Endpoint**: `GET /{address}`
   - **Response**: Returns the current temperature (Celsius), maximum temperature, minimum temperature, feels like temperature, and description for the specified location.
2. **Get Weather Data for a Specific Date Range**:
   - **Endpoint**: `GET /{address}/{startTime}/{endTime}`
   - **Response**: Returns the weather data for each day within the specified date range, including temperatures, descriptions, etc.
3. **Get 14-Day Weather Data for a Specific Location**:
   - **Endpoint**: `GET /{address}/all`
   - **Response**: Returns the weather data from the day before up to the next 13 days for the specified location.

### 6. Data Format

Each weather data entry contains the following fields:

- **address**: Location name
- **datetime**: Date and time
- **tempmax**: Maximum temperature (Celsius)
- **tempmin**: Minimum temperature (Celsius)
- **temp**: Current temperature (Celsius)
- **feelslikemax**: Maximum feels-like temperature (Celsius)
- **feelslikemin**: Minimum feels-like temperature (Celsius)
- **feelslike**: Current feels-like temperature (Celsius)
- **description**: Weather description

### 7. Caching

This project uses the Spring Cache caching mechanism. All query requests (based on address, date, and time range) are cached. The caching strategy is as follows:

- **Single-Day Weather Cache**: The cache is set with `@Cacheable(value = "weather_single", key = "#address")` for each address's current day weather.
- **Date Range Weather Cache**: The cache is set with `@Cacheable(value = "weather", key = "#address+startTime+endTime")` for the weather data of a specific address and date range.
- **14-Day Weather Cache**: The cache is set with `@Cacheable(value = "weatherAll", key = "#address")` for the 14-day weather data for a specific address.

The purpose of caching is to reduce the frequency of external API calls and improve performance. The cache expiry time depends on the caching implementation used (such as Redis or Caffeine). If no expiry time is configured, the cache will persist indefinitely.

### 8. Error Handling

If an error occurs while fetching or parsing the weather data, the application will throw a `RuntimeException` and return an HTTP 500 error response. You can customize the error handling mechanism to provide more user-friendly error messages as needed.

### 9. Project Extensions

- **Weather Data Source**: Currently, the project uses the Visual Crossing Weather API, but it can be replaced with other weather APIs if necessary.
- **Cache Expiry Policy**: The default cache configuration in Spring Cache can be customized according to specific requirements to set cache expiry times.
- **Other Features**: You can extend the project to support additional features such as historical weather data, future weather forecasting, etc.

### 10. Dependencies

Main dependencies of the project:

- Spring Boot
- Jackson (for JSON processing)
- Spring Cache (for cache management)
- RestTemplate (for making HTTP requests)

These dependencies are managed via `pom.xml`.

------

## Summary

This project is a simple weather query service that supports fetching the weather for a specific location for the current day, a date range, or a 14-day period. The caching mechanism provided by Spring Cache improves performance and reduces the number of external API calls. With simple configuration and API endpoints, users can easily query weather data, and the project can be extended to include additional features as needed.

This is a solution to a project challenge in [roadmap.sh](https://roadmap.sh/projects/weather-api-wrapper-service).
