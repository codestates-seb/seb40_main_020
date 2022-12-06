# 💸 One Coin (모의 코인 투자 서비스)
<p align="center"><img width="600px" src="https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F98d9f203-908f-41a7-b277-88e56c3fb7ae%2Fone.png?id=737f09a3-df54-4684-9b7e-f615fbc2d00c&table=block&spaceId=82d63a72-8254-4cde-bf1e-b2597b7c099c&width=2000&userId=8551cd99-d6ef-45e1-b0fd-76c17a8c615b&cache=v2"></p>

배포 링크 : **[ONECOIN 바로가기](<http://projectonecoin.s3-website.ap-northeast-2.amazonaws.com/>)**

데모 영상 : <https://youtu.be/j2MjyMpPnfo/>

프로젝트 기간 : 2022.11.8 ~ 2022.12.07
<br><br>

## Service Introduction

### 코인 열풍! 나도 해보고 싶은데, 일단 시작해볼까?

암호 화폐에 대한 관심이 갈 수록 증가하여, 많은 사람들이 투자의 수단으로 참여하고 있습니다. 그러나, 잘 준비되지 않은 상태로 무작정 코인 투자를 시작하여 피해를 보는 사람들도 함께 많아지고 있습니다.

그래서 “**`모의 코인 투자`**” 서비스를 준비했습니다. 저희 서비스를 통해 실전처럼 위험 부담 없이 연습을 해보며 코인에 대해 배워갈 수 있습니다. 실제 거래되고 있는 상품과 가격 정보를 그대로 반영하여, 실제 마켓에서 해당 가격에 거래가 일어났을 때 거래가 체결됩니다. 또한, 모의 투자와 동시에 할 수 있는 “**`실시간 채팅`**” 서비스를 통해, 여러 사람들과 코인에 대한 의견을 공유할 수 있습니다. 궁극적으로 저희는 이 서비스를 통해, 실제 투자 이전에 연습을 해봄으로써 더욱 신중히 고민을 할 수 있도록 도울 수 있을 것이라 기대합니다.

<br><br>

## Team Members
|Front-end|Front-end|Front-end|Back-end|Back-end|Back-end|
|:--:|:--:|:--:|:--:|:--:|:--:|
|(팀장)서재홍|김민상|민다영|김기홍|남기범|김지환|
|<img src="https://avatars.githubusercontent.com/u/107832252?v=4" width=150>|<img src="https://avatars.githubusercontent.com/u/64800318?v=4" width=150>|<img src="https://avatars.githubusercontent.com/u/32324401?v=4" width=150>|<img src="https://avatars.githubusercontent.com/u/99868638?v=4" width=150>|<img src="https://avatars.githubusercontent.com/u/101033262?v=4t" width=150>|<img src="https://avatars.githubusercontent.com/u/66046153?v=4" width=150>|
|[@jaehongg](https://github.com/jaehongg)|[@minsang98](https://github.com/minsang98)|[@MINDA01](https://github.com/MINDA01)|[@broaden-horizon](https://github.com/broaden-horizon)|[@GIVEN53](https://github.com/GIVEN53)|[@kjh42447](https://github.com/kjh42447)|
<br><br>

## ER-Diagram
![image](https://user-images.githubusercontent.com/99868638/205669986-00876c20-adc6-4dc3-bf5b-70bd0477ef66.png)

<br><br>

## NoSQL Modeling
![image](https://user-images.githubusercontent.com/99868638/205670653-76c48084-fcd8-4313-8986-9ecfe991af6b.png)

<br><br>

## Built With
<p align="center"><img width="600px" src="https://user-images.githubusercontent.com/99868638/205670347-2099b735-e0db-4e30-8970-aef59366cc0d.png"></p>

<br><br>

## Service Architecture
<img src="https://user-images.githubusercontent.com/99868638/205670499-f26f01a7-1136-40f7-b17b-6fbb543f1177.png">

<br><br>

## Documentation
[요구사항 정의서](https://given53.notion.site/84815a9ddea94523a5b42ff09b5f925d?v=d506d3c461c041baa5f01e7cd5ea1808)

[화면 정의서](https://www.figma.com/file/92A3jCXPtcod2gesRwR6X7/1Coin?node-id=114%3A2)

[API 명세서](https://given53.notion.site/1df69396c52a4528a716de7c07cb6d69?v=53ae6a171547493d82822a3c18bc6d3b)

<br><br>


## Commit Message Convention
    - ✨feat: 새로운 기능 추가, 기존의 기능을 요구 사항에 맞추어 수정
    - 🐛fix: 기능에 대한 버그 수정
    - 👷build: 빌드 관련 수정
    - 📦chore: 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore
    - 📝docs: 문서(주석) 수정
    - 🎨style: 코드 스타일, 포맷팅에 대한 수정
    - ♻️refactor: 기능의 변화가 아닌 코드 리팩터링 ex) 변수 이름 변경
    - 🔖release: 버전 릴리즈
    - 🧪test: 테스트 코드관련
    - 🔀merge: 병합

<br><br>

## Git-flow
Git flow는 main과 dev 두 브랜치와 dev 아래에 front와 back, 그 밑에 feature 보조 브랜치로 이루어져 있다.

![image](https://user-images.githubusercontent.com/107832252/201564387-465833c6-cdbd-48a2-a790-48c8a11b6e2b.png)

---
> 1. **dev 브랜치의 각자(front,back)에서 개발이 시작된다.**
> 2. **기능 구현이나 버그가 발생하면 issue를 작성한다.**
> 3. **팀원들이 issue 해결을 위해 dev 브랜치 밑에서 생성한 front,back/feat/{구현기능} 브랜치에서 개발을 하고 commit log를 작성한다.**
> 4. **push를 하면 pull request를 날릴 수 있다.**
> 5. **pull request를 통해 팀원들 간의 피드백, 버그 찾는 과정이 진행된다.
>    release 브랜치가 없으므로 이 과정이 탄탄하게 진행되어야 한다.**
> 6. **모든 리뷰가 이루어지면, merge하기 전에 배포를 통해 최종 테스트를 진행한다.**
> 7. **테스트까지 진행되면 main 브랜치에 머지한다.**
---