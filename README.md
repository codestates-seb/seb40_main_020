#  One Coin (모의 코인 투자 서비스)

배포 링크 : <http://projectonecoin.s3-website.ap-northeast-2.amazonaws.com/>

데모 영상 : <https://youtu.be/j2MjyMpPnfo/>

프로젝트 기간 : 2022.11.8 ~ 2022.12.07

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

## NoSQL Modeling
![image](https://user-images.githubusercontent.com/99868638/205670653-76c48084-fcd8-4313-8986-9ecfe991af6b.png)

## Built With
<img width="800px" src="https://user-images.githubusercontent.com/99868638/205670347-2099b735-e0db-4e30-8970-aef59366cc0d.png">

## Service Architecture
<img src="https://user-images.githubusercontent.com/99868638/205670499-f26f01a7-1136-40f7-b17b-6fbb543f1177.png">

## API Documentation
[API 명세서 Spreadsheets]

<br><br>

---
- **commit message convention**
    
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

---
## ✅ 참여 방법

### Git flow (수정)

- Git flow는 main과 dev 두 중요 브랜치와 dev밑에 front,back 그 밑에 feature 보조 브랜치로 이루어져 있다.

![image](https://user-images.githubusercontent.com/107832252/201564387-465833c6-cdbd-48a2-a790-48c8a11b6e2b.png)


자세한 Git flow의 과정을 알아보자.

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
### main 브랜치

사용자에게 언제든 제품으로 출시할 수 있는 브랜치

main 브랜치는 사용자에게 언제든 배포할 수 있는 브랜치입니다. 회사에 따라서 master, prod, production등 다양하게 불립니다. “언제든 배포할 수 있다"의 의미는 회사 별로, 팀 별로 다를 수 있습니다. 다만, 최소한의 기준을 세워볼 수 있습니다.

대표적인 기능이 완성되었다.
기존 기획했던 레이아웃이나 전체적인 디자인이 얼추 완성되었다.
클라이언트, 서버, 데이터베이스가 공개된 웹에서 정상적으로 통신할 수 있다.
최소한의 보안이 마련되었다.
브라우저에서 개발 버전에서 사용하던 secret이나 유저의 비밀번호가 노출되는가?
유저의 기밀 정보 조회를 위해 인증 토큰, 세션이 꼭 필요한가?
이렇게 일정 기준을 충족했고, 핵심 기능이 완성되었으면 main 브랜치로 배포를 할 수 있습니다.

---
### dev 브랜치

다음 버전 배포를 위한 "개발 중!" 브랜치

dev 브랜치는 다음 버전 배포를 위한 "개발 중!" 브랜치입니다. main 브랜치에서부터 브랜칭을 하는게 보통이며, 가능하면 프로젝트 팀원과 프론트엔드와 백엔드의 결과를 합쳐서 확인해볼 수 있을 정도로 준비가 되어야 합니다. CI/CD 파이프라인이 잘 구축되어 있다면 dev 브랜치의 코드도 배포를 해두고 수시로 확인할 수도 있습니다.

main 브랜치와 dev 브랜치는 Github 리포지토리에 늘 업데이트 되어있어야 하며, 팀원의 코드 리뷰를 받고 진행하는 것이 정석입니다. 엄밀한 코드 리뷰가 어렵다면, 같이 모여서 코드에 대해서 이야기를 나누고, 배포 상황을 점검하는 스텐드업 회의를 열어도 좋습니다. 너무 격식이 있을 필요는 없지만, 가능하면 모든 팀원이 확인 가능하게 “어떤 코드의 어디를 왜 이렇게 바꿨으면 좋겠다.”라는 코멘트를 Github Pull Request에 남기는 것을 권장합니다.

---
### 보조 브랜치

feature 브랜치
feature 브랜치는 기능 개발, 리펙토링, 문서 작업, 단순 오류 수정 등 다양한 작업을 기록하기 위한 브랜치입니다. 분류를 세세하게 나누기를 원하는 회사에서는 refactor, fix, docs, chore와 같이 세세하게 커밋 메시지나 브랜치 명에 prefix를 달기도 합니다. 아래는 feature 브랜치 이름과 커밋 메시지의 예시입니다. 더 많은 사례는 Conventional Commits 에 대해서 확인하실 수 있습니다.

```
hash (브랜치 명) 커밋 메시지
2f85eea (feat/create-todo) feat: Todo 추가 기능
2ad0805 (fix/var-name) fix: 변수 네이밍 컨벤션에 맞게 변수명 변경 (ismale => isMale)
e7ce3ad (refactor) refactor: 불필요한 for 루프 삭제
```

feature 브랜치는 보통 각 개인의 로컬 리포지토리에서 만들고 작업합니다. feature 브랜치는 기능 개발을 위한 브랜치이기 때문에 2명 이상 같이 작업하는 경우가 드물어서, 브랜치 생성이나 삭제에 대해서 너무 두려워 할 필요는 없습니다. 작은 기능이라도 브랜치를 새로 만들고, 자주 커밋하고, 자주 원격 Github 리포지토리에 push하여 팀원들과 결과를 공유하는 것이 바람직합니다. 개인 로컬 리포지토리에서 너무 오래 작업을 하다보면, 쉽게 발견할 수 있는 오류도 발견이 되지 않곤 합니다. 더 나은 코드를 위해 피드백 받는 것을 두려워하지 맙시다.

회사에 따라서 커밋 기록을 남기는 일반적인 rebase-and-merge, 기능마다 깔끔하게 커밋을 남기기를 원해서 커밋 기록을 정리하는 squash-and-merge등 다양한 merge 전략이 있습니다. 많은 경우 feature 브랜치는 머지하고 나서 삭제하지만, 복원해야 할 필요성이 있는 경우는 남겨두기도 합니다.
