package org.example.kb6spring.repository.member.v1;

import org.example.kb6spring.domain.member.MemberEntity;

import java.util.ArrayList;
import java.util.List;

public class MemberRepositoryV0 {
    private static MemberRepositoryV0 instance;

    private MemberRepositoryV0() {}

    public static MemberRepositoryV0 getInstance() {
        if (instance == null) {
            instance = new MemberRepositoryV0();
        }

        return instance;
    }

    public List<MemberEntity> getMemberList() {
        List<MemberEntity> memberList = new ArrayList<>();

        memberList.add(new MemberEntity(1L, "ronaldo@example.com", "호날두", "플래티넘", 300000000L));
        memberList.add(new MemberEntity(2L, "sjk@example.com", "송중기", "골드", 3000000L));
        memberList.add(new MemberEntity(3L, "xenosign@example.com", "이효석", "아이언", 10L));

        return memberList;
    }
}
