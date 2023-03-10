# 체스
## 소개
자바 언어로 만든 체스 게임입니다. 체스게임은 터미널 환경에서 출력됩니다.

## 명령어
체스 기물 이동 : [열][행]->[열][행]
- ex) A2->A3

체스 기물의 이동 경로 확인 : ?[열][행]
- ex) ?A2

## TODOLIST
- [x] initBoard 구현
- [x] move 구현
- [x] 사용자 명령어 입력
- [x] 사용자 명령어 검증
- [x] 기물 이동시 상대 기물 잡아먹기
- [x] 룩, 비숍, 나이트, 퀸 추가
- [x] 목적지 중간에 같은 기물이 있거나 다른 색상의 기물이 있는 것
- [x] 폰 승진
- [x] possiblePosition
- [x] isMovable(Piece)로 개선
  - [x] Pieces.find(Position)로 탐색한 기물이 없는 경우 해결
  - [x] 중간에 기물 검사 Pieces에서 Piece 객체로 내리기
- [x] Board.move(Position, Position) 호출시 비어있는 곳에서 시작하여 이동시
  이동이 안되는 테스트 코드 추가
- [x] 승리 추가
- [x] 점수 추가

## 추가 기능 요구사항
- [X] king 추가
- [x] pawn 대각선으로 기물을 잡기
- [ ] pawn 앙파상 추가
- [x] knight 이동시 전진하는 칸이 막혀 있어도 이동이 가능하게 개선
- [ ] pawn 첫턴한정 2칸 움직이기

## 요구사항 분석
- pawn이 대각선으로 이동하여 기물을 잡기 위해서는 대각선 방향에 상대방 기물이 존재해야 한다. 그외의 경우는 직진밖에 못한다.

## 빌드 및 실행
실행 환경 : ubuntu server 22
```shell
~ $ ./gradlew build
~ $ java -jar build/libs/chess-1.0-SNAPSHOT.jar
```

![image](images/img.png)

