package com.assignment.walnut.repository;

import com.assignment.walnut.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
}
