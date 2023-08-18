package com.example.forecasty.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

fun getTheDateFromTimestamp(timestamp: Long): String {
    val date = Date(timestamp * 1000)

    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(date)
}

fun getTimeFromUnixTimestamp(unixTimestamp: Long): String {
    val date = Date(unixTimestamp * 1000)
    val sdf = SimpleDateFormat("HH:mm:ss")
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(date)
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities =
        connectivityManager.activeNetwork ?: return false
    val activeNetwork =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

    return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

fun getCountryFullName(countryCode: String): String? {
    val countryMap = mapOf(
        "DZ" to "Algeria",
        "AO" to "Angola",
        "BJ" to "Benin",
        "BW" to "Botswana",
        "BF" to "Burkina Faso",
        "BI" to "Burundi",
        "CV" to "Cabo Verde",
        "CM" to "Cameroon",
        "CF" to "Central African Republic",
        "TD" to "Chad",
        "KM" to "Comoros",
        "CD" to "Congo, Democratic Republic of the",
        "CG" to "Congo, Republic of the",
        "CI" to "Cote d'Ivoire (Ivory Coast)",
        "DJ" to "Djibouti",
        "EG" to "Egypt",
        "GQ" to "Equatorial Guinea",
        "ER" to "Eritrea",
        "SZ" to "Eswatini (formerly Swaziland)",
        "ET" to "Ethiopia",
        "GA" to "Gabon",
        "GM" to "Gambia",
        "GH" to "Ghana",
        "GN" to "Guinea",
        "GW" to "Guinea-Bissau",
        "KE" to "Kenya",
        "LS" to "Lesotho",
        "LR" to "Liberia",
        "LY" to "Libya",
        "MG" to "Madagascar",
        "MW" to "Malawi",
        "ML" to "Mali",
        "MR" to "Mauritania",
        "MU" to "Mauritius",
        "MA" to "Morocco",
        "MZ" to "Mozambique",
        "NA" to "Namibia",
        "NE" to "Niger",
        "NG" to "Nigeria",
        "RW" to "Rwanda",
        "ST" to "Sao Tome and Principe",
        "SN" to "Senegal",
        "SC" to "Seychelles",
        "SL" to "Sierra Leone",
        "SO" to "Somalia",
        "ZA" to "South Africa",
        "SS" to "South Sudan",
        "SD" to "Sudan",
        "TZ" to "Tanzania",
        "TG" to "Togo",
        "TN" to "Tunisia",
        "UG" to "Uganda",
        "ZM" to "Zambia",
        "ZW" to "Zimbabwe",
        "AL" to "Albania",
        "AD" to "Andorra",
        "AT" to "Austria",
        "BY" to "Belarus",
        "BE" to "Belgium",
        "BA" to "Bosnia and Herzegovina",
        "BG" to "Bulgaria",
        "HR" to "Croatia",
        "CY" to "Cyprus",
        "CZ" to "Czech Republic",
        "DK" to "Denmark",
        "EE" to "Estonia",
        "FI" to "Finland",
        "FR" to "France",
        "DE" to "Germany",
        "GR" to "Greece",
        "HU" to "Hungary",
        "IS" to "Iceland",
        "IE" to "Ireland",
        "IT" to "Italy",
        "LV" to "Latvia",
        "LI" to "Liechtenstein",
        "LT" to "Lithuania",
        "LU" to "Luxembourg",
        "MT" to "Malta",
        "MD" to "Moldova",
        "MC" to "Monaco",
        "ME" to "Montenegro",
        "NL" to "Netherlands",
        "MK" to "North Macedonia",
        "NO" to "Norway",
        "PL" to "Poland",
        "PT" to "Portugal",
        "RO" to "Romania",
        "RU" to "Russia",
        "SM" to "San Marino",
        "RS" to "Serbia",
        "SK" to "Slovakia",
        "SI" to "Slovenia",
        "ES" to "Spain",
        "SE" to "Sweden",
        "CH" to "Switzerland",
        "UA" to "Ukraine",
        "GB" to "United Kingdom",
        "VA" to "Vatican com.example.forecasty.pojo.forecast.City",
        "AF" to "Afghanistan",
        "AM" to "Armenia",
        "AZ" to "Azerbaijan",
        "BH" to "Bahrain",
        "BD" to "Bangladesh",
        "BT" to "Bhutan",
        "BN" to "Brunei",
        "KH" to "Cambodia",
        "CN" to "China",
        "CY" to "Cyprus",
        "GE" to "Georgia",
        "IN" to "India",
        "ID" to "Indonesia",
        "IR" to "Iran",
        "IQ" to "Iraq",
        "IL" to "Palestine",
        "JP" to "Japan",
        "JO" to "Jordan",
        "KZ" to "Kazakhstan",
        "KW" to "Kuwait",
        "KG" to "Kyrgyzstan",
        "LA" to "Laos",
        "LB" to "Lebanon",
        "MY" to "Malaysia",
        "MV" to "Maldives",
        "MN" to "Mongolia",
        "MM" to "Myanmar (Burma)",
        "NP" to "Nepal",
        "KP" to "North Korea",
        "OM" to "Oman",
        "PK" to "Pakistan",
        "PH" to "Philippines",
        "QA" to "Qatar",
        "SA" to "Saudi Arabia",
        "SG" to "Singapore",
        "KR" to "South Korea",
        "LK" to "Sri Lanka",
        "SY" to "Syria",
        "TW" to "Taiwan",
        "TJ" to "Tajikistan",
        "TH" to "Thailand",
        "TR" to "Turkey",
        "TM" to "Turkmenistan",
        "AE" to "United Arab Emirates",
        "UZ" to "Uzbekistan",
        "VN" to "Vietnam",
        "YE" to "Yemen",
        "AG" to "Antigua and Barbuda",
        "BS" to "Bahamas",
        "BB" to "Barbados",
        "BZ" to "Belize",
        "CA" to "Canada",
        "CR" to "Costa Rica",
        "CU" to "Cuba",
        "DM" to "Dominica",
        "DO" to "Dominican Republic",
        "SV" to "El Salvador",
        "GD" to "Grenada",
        "GT" to "Guatemala",
        "HT" to "Haiti",
        "HN" to "Honduras",
        "JM" to "Jamaica",
        "MX" to "Mexico",
        "NI" to "Nicaragua",
        "PA" to "Panama",
        "KN" to "Saint Kitts and Nevis",
        "LC" to "Saint Lucia",
        "VC" to "Saint Vincent and the Grenadines",
        "TT" to "Trinidad and Tobago",
        "US" to "United States", "AR" to "Argentina",
        "BO" to "Bolivia",
        "BR" to "Brazil",
        "CL" to "Chile",
        "CO" to "Colombia",
        "EC" to "Ecuador",
        "GY" to "Guyana",
        "PY" to "Paraguay",
        "PE" to "Peru",
        "SR" to "Suriname",
        "UY" to "Uruguay",
        "VE" to "Venezuela",
        "AU" to "Australia",
        "FJ" to "Fiji",
        "KI" to "Kiribati",
        "MH" to "Marshall Islands",
        "FM" to "Micronesia",
        "NR" to "Nauru",
        "NZ" to "New Zealand",
        "PW" to "Palau",
        "PG" to "Papua New Guinea",
        "WS" to "Samoa",
        "SB" to "Solomon Islands",
        "TO" to "Tonga",
        "TV" to "Tuvalu",
        "VU" to "Vanuatu",
        "AQ" to "Antarctica"
    )
    return countryMap[countryCode] ?: countryCode
}