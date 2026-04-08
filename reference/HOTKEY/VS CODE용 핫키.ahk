;━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
; - 기본 capslock기능을 꺼둠
;━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
SetStoreCapslockMode false
SetCapsLockState "AlwaysOff"

;━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
; - capslock + f 키로 기본 capslock를 키고 끔
;━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
CapsLock & f::
{
    if GetKeyState("CapsLock", "T")
        SetCapsLockState "AlwaysOff"
    else
        SetCapsLockState "AlwaysOn"
}

;━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
; - Capslock키를 누르고 있을 경우 (방향키 및 커맨드)
;━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
#HotIf GetKeyState("CapsLock", "P")

i::Send "{Up}"
j::Send "{Left}"
k::Send "{Down}"
l::Send "{Right}"

q & j::Send "^{Left}" ; 캡스락 + q + j : 빠르게 이동
q & l::Send "^{Right}"

w::Send "{PgUp}"
a::Send "{Home}"
s::Send "{PgDn}"
d::Send "{End}"

Left::Send "{Home}"
Right::Send "{End}"
Up::Send "{PgUp}"
Down::Send "{PgDn}"

Enter::Send "^{Enter}" ; - 커서를 끝까지 옮기지 않고 그자리에서 바로 엔터처리
o::Send "{BackSpace}"
u::Send "{Del}"

#HotIf ; 조건부 핫키 종료