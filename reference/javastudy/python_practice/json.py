information={
    "Profile": {
        "name": "Evan",
        "birth_date": "1994",
        "hobby": [
            "playing soccer",
            "listening music",
            "playing cello"
        ],
        "current_address": {
            "nation": "Republic of Korea",
            "province": "Gyeong-gi",
            "City": "Yong-in"
        },
        "birth_place": {
            "nation": "Republic of Korea",
            "province": "Ulsan",
            "City": "Jung-gu"
        }
    },
    "Resume": {
        "name": "Evan",
        "work_experience": {
        "Blizzard Entertainment": {
            "period": 2.5,
            "department": "Customer Service"
        },
        "Josun Palace, a Luxury Collection Hotel": {
            "period": 3.5,
            "department": "Human Resources"
        }
        },
        "education": {
            "highschool": "Evergreen Highschool",
            "university": "Hankuk University of Foreign Studies"
        },
        "experience": [
            "Research Asssistant(Internship)",
            "Localization(part-time)"
        ]
    }
}

print(information)
print(information["Profile"]["current_address"]["nation"], information["Profile"]["current_address"]["province"], information["Profile"]["current_address"]["City"])
print(2030 - int(information["Profile"]["birth_date"]) + 1)