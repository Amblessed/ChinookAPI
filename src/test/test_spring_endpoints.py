import requests
import allure

BASE_URL = "http://localhost:8080/api/v1/customer"

@allure.feature('Get All Customers API')
@allure.story('GET /api/v1/customer')
def test_get_all_customers():
    """Test GET all customers"""
    response = requests.get(f"{BASE_URL}", timeout=10)
    assert response.status_code == 200
    customers: list = response.json()
    assert isinstance(customers, list)
    assert "customerId" in customers[0]
    assert len(customers) > 0
    assert len(customers) == 59


@allure.feature('Get Customers By Country API')
@allure.story('GET /api/v1/customer/country')
def test_get_customers_by_country():
    """Test GET all customers"""
    params = {'country': 'usa'}
    response = requests.get(f"{BASE_URL}/country", timeout=10, params=params)
    assert response.status_code == 200
    customers: list = response.json()
    assert isinstance(customers, list)
    assert len(customers) == 13
    assert all(customer["country"] == "USA" for customer in customers)

@allure.feature('Get Customers By City API')
@allure.story('GET /api/v1/customer/city')
def test_get_customers_by_city():
    """Test GET all customers"""
    params = {'city': 'prague'}
    response = requests.get(f"{BASE_URL}/city", timeout=10, params=params)
    assert response.status_code == 200
    customers: list = response.json()
    assert isinstance(customers, list)
    assert len(customers) == 2
    assert all(customer["city"].lower() == "prague" for customer in customers)
