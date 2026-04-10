import random
import pandas as pd
import os

# 데이터 구성 요소
adverbs = ["정말", "진짜", "너무", "완전", "상당히", "꽤", "오늘따라", "갑자기", "솔직히", "확실히", "왠지", "괜히", "무척", "엄청", "되게", "참", "유난히", "그냥", "다행히", "역시"]
positive_contexts = ["맛있는 걸 먹어서", "날씨가 맑아서", "친구를 만나서", "시험을 잘 봐서", "칭찬을 들어서", "월급이 들어와서", "푹 자고 일어나서", "좋은 노래를 들어서", "선물을 받아서", "여행 계획을 짜서", "오랜만에 쉬어서", "운동을 하고 나서", "가족과 시간을 보내서", "일이 잘 풀려서", "도움을 받아서", "새로운 것을 배워서", "산책을 해서", "할 일을 다 끝내서", "좋은 소식을 들어서", "성과를 인정받아서"]
negative_contexts = ["비가 계속 와서", "차가 막혀서", "늦잠을 자서", "물건을 잃어버려서", "친구랑 싸워서", "몸이 안 좋아서", "일이 너무 많아서", "상사에게 혼나서", "돈을 낭비해서", "시험을 망쳐서", "잠을 못 자서", "나쁜 소식을 들어서", "배가 아파서", "계획이 틀어져서", "버스를 놓쳐서", "물건이 고장나서", "약속이 취소돼서", "길을 잃어서", "사람이 너무 많아서", "중요한 파일을 날려서"]
positive_predicates = ["기분이 좋아", "행복해", "신이 나", "즐거워", "마음이 편해", "뿌듯해", "힘이 나", "상쾌해", "감동적이야", "사랑스러워", "만족스러워", "기대돼", "설레", "최고야", "웃음이 나", "든든해", "활기차", "가벼워", "따뜻해", "안심이 돼"]
negative_predicates = ["우울해", "슬퍼", "화가 나", "짜증 나", "피곤해", "지쳐", "힘들어", "답답해", "괴로워", "속상해", "무기력해", "불안해", "걱정돼", "심심해", "귀찮아", "실망스러워", "어이없어", "막막해", "서운해", "억울해"]
endings = [".", "!", "...", "!!", " ㅠㅠ", " ㅎㅎ", " ^ ^"]

def generate_random_sentence(label):
    adv = random.choice(adverbs)
    ending = random.choice(endings)
    ctx = random.choice(positive_contexts if label == 1 else negative_contexts)
    pred = random.choice(positive_predicates if label == 1 else negative_predicates)
    return f"{adv} {ctx} {pred}{ending}"

# 데이터 생성 및 저장
data = [[generate_random_sentence(random.randint(0, 1)), random.randint(0, 1)] for _ in range(9999)]
df = pd.DataFrame(data)
filename = 'generated_sentiment_9999.csv'
df.to_csv(filename, index=False, header=False, encoding='utf-8-sig')

# 결과 출력
print("-" * 50)
print(f"✅ 파일 생성 완료: {len(df)}개 문장")
print(f"📍 파일 저장 위치: {os.path.abspath(filename)}")
print("-" * 50)