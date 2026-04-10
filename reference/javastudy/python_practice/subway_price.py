subway_order = {
    "Subway_Menu": [
        {
            "menu": "BMT",
            "size": 15,
            "bread type": "honey oat",
            "isToastingTrue": True,
            "cheese": "american cheese",
            "vegetable": [
                "lettuce",
                "tomato",
                "onion"
            ],
            "sauce": [
                "ranch",
                "black pepper"
            ],
            "price": 8100
        },
        {
            "extra": {
                "egg mayonnaise": 2000,
                "avocado": 1500
            },
        },
        {
            "isSetTrue": True,
            "set": {
                "cookie_drink": 2000
            },
        }
    ]
}


thirtycm_BMT_price = (int(subway_order["Subway_Menu"][0]["price"]) * 2 - 1000)
extra_price = (int(subway_order["Subway_Menu"][1]["extra"]["egg mayonnaise"]) +
                int(subway_order["Subway_Menu"][1]["extra"]["avocado"]))
set_price = (int(subway_order["Subway_Menu"][2]["set"]["cookie_drink"]))

total_price = thirtycm_BMT_price + extra_price + set_price

print(f"30cm BMT 가격: {thirtycm_BMT_price}")
print(f"추가 가격: {extra_price}")
print(f"세트 가격: {set_price}")
print("----------------")
print("총 가격: ", total_price, "원")