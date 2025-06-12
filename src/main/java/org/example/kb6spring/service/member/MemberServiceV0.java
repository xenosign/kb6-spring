package org.example.kb6spring.service.member;

import org.example.kb6spring.domain.member.MemberEntity;
import org.example.kb6spring.dto.member.MemberDto;
import org.example.kb6spring.repository.member.v1.MemberRepositoryV0;

import java.util.ArrayList;
import java.util.List;

public class MemberServiceV0 {
    private static MemberServiceV0 instance;
    private final MemberRepositoryV0 memberRepository;

    public MemberServiceV0() {
        this.memberRepository = MemberRepositoryV0.getInstance();
    }

    public static MemberServiceV0 getInstance() {
        if (instance == null) {
            instance = new MemberServiceV0();
        }

        return instance;
    }

    public List<MemberDto> getMemberList() {
        List<MemberEntity> entityList = memberRepository.getMemberList();
        List<MemberDto> dtoList = new ArrayList<>();

        for (MemberEntity entity : entityList) {
            MemberDto dto = new MemberDto();
            dto.setName(entity.getName());
            dto.setEmail(entity.getEmail());
            dtoList.add(dto);
        }

        return dtoList;
    }
}
