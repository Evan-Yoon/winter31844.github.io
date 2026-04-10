subway_order = {
    "Subway_Menu": [
        {
            "menu": "BMT",
            "size": 15,
            "bread type": "honey oat",
            "isToastingTrue": True,
            "isSetTrue": True,
            "set": {
                "cookie": "double chocolate chip cookie",
                "drink": "coca cola zero"
            },
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
            "extra": [
                "egg mayonnaise",
                "avocado"
            ],
            "price": 8100
        }
    ]
}

points = 0

print(subway_order)
print("30cm BMT가격: ", int(subway_order["Subway_Menu"][0]["price"])*2 - 1000, "원")
print("마일리지 적립: ", int(subway_order["Subway_Menu"][0]["price"])*0.01, "포인트 적립됨\n"
        "적립 후 마일리지: ", points + int(subway_order["Subway_Menu"][0]["price"])*0.01, "포인트")