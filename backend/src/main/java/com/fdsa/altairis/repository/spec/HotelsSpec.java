package com.fdsa.altairis.repository.spec;

import com.fdsa.altairis.model.Hotel;
import org.springframework.data.jpa.domain.Specification;

public final class HotelsSpec {

    private HotelsSpec() {}

    public static Specification<Hotel> nameLike(String q) {
        return (root, query, cb) -> (q == null || q.isBlank())
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + q.trim().toLowerCase() + "%");
    }

    public static Specification<Hotel> codeLike(String q) {
        return (root, query, cb) -> (q == null || q.isBlank())
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("code")), "%" + q.trim().toLowerCase() + "%");
    }

    public static Specification<Hotel> cityIdEq(Long cityId) {
        return (root, query, cb) -> (cityId == null)
                ? cb.conjunction()
                : cb.equal(root.get("city").get("id"), cityId);
    }

    public static Specification<Hotel> statusEq(String status) {
        return (root, query, cb) -> (status == null || status.isBlank())
                ? cb.conjunction()
                : cb.equal(root.get("status"), status);
    }

}
