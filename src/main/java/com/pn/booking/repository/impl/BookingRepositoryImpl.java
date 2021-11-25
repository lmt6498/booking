package com.pn.booking.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.pn.booking.common.utils.CommonUtils;
import com.pn.booking.model.constant.BookingStatus;
import com.pn.booking.model.dto.request.filter.BookingFilterRequest;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.repository.AbstractRepository;
import com.pn.booking.repository.custom.BookingRepositoryCustom;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class BookingRepositoryImpl extends AbstractRepository implements BookingRepositoryCustom {
  
  @Override
  public List<Booking> search(BookingFilterRequest request) {
    Query query = createQuery(request, false);
    if (query == null) {
      return new ArrayList<Booking>();
    } else {
      return (ArrayList<Booking>) query.getResultList();
    }
  }

  @Override
  public Long count(BookingFilterRequest request) {
    Query query = createQuery(request, true);
    if (query == null) {
      return 0L;
    } else {
      return (Long) query.getSingleResult();
    }
  }

  @Override
  public Long countToBeCheckedIn(BookingFilterRequest request) {
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT COUNT(DISTINCT(result.id)) as count, status, start_time, end_time FROM ( ");
    sql.append("SELECT b.id, status, start_time, end_time FROM bookings b ");
    sql.append("JOIN bookings_has_customers bc ON bc.bookings_id = b.id ");
    sql.append("JOIN customers c ON c.id = bc.customers_id AND c.deleted_at IS NULL ");
    sql.append(getFilterQuery(request));
    sql.append(") result ");
    sql.append("WHERE status = 'CONFIRMED' ");

    // if (StringUtils.isEmpty(request.getSearchKey())) {
      // to be checked in, exluding confirmed only
      sql.append("AND :startDateTime = :startDateTime "); // dirty fix for now for missing parameter
      sql.append("AND (DATE(DATE_ADD(start_time, INTERVAL :offset HOUR)) = DATE(DATE_ADD(now(), INTERVAL :offset HOUR)) "); 
      // missed check-in
      sql.append("OR (DATE_ADD(start_time, INTERVAL 1 HOUR) < now() AND TIMESTAMP(:endDateTime) >= TIMESTAMP(end_time)) "); 
      sql.append(") ");   
    // }

    Query query = nativeQuery(sql.toString());
    query.setParameter("businessId", request.getBusinessId());

    if (!StringUtils.isEmpty(request.getSearchKey())) {
      query.setParameter("searchKey", request.getSearchKey());
    }
    
    if (!StringUtils.isEmpty(request.getStartDateTime()) && !StringUtils.isEmpty(request.getEndDateTime())) {
      query.setParameter("startDateTime", CommonUtils.convertStringToTimestampUTC(request.getStartDateTime()));
      query.setParameter("endDateTime", CommonUtils.convertStringToTimestampUTC(request.getEndDateTime()));
    }

    if (!StringUtils.isEmpty(request.getStartDateTime())) {
    Integer offset = CommonUtils.getOffset(request.getStartDateTime());
    query.setParameter("offset", offset);
    }

    List<Object[]> result = query.getResultList();
    if (result != null && result.size() > 0) {
      return ((BigInteger) result.get(0)[0]).longValue();
    } else {
      return 0L;
    }
  }

  @Override
  public Long countToBeCheckedOut(BookingFilterRequest request) {
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT COUNT(DISTINCT(result.id)) as count, status, start_time, end_time FROM ( ");
    sql.append("SELECT b.id, status, start_time, end_time FROM bookings b ");
    sql.append("JOIN bookings_has_customers bc ON bc.bookings_id = b.id ");
    sql.append("JOIN customers c ON c.id = bc.customers_id AND c.deleted_at IS NULL ");
    sql.append(getFilterQuery(request));
    sql.append(") result ");
    sql.append("WHERE status = 'CHECKED_IN' ");
    
    // if (StringUtils.isEmpty(request.getSearchKey())) {
      sql.append("AND (");
        sql.append("(TIMESTAMP(end_time) >= TIMESTAMP(:startDateTime) AND TIMESTAMP(end_time) <= TIMESTAMP(:endDateTime))"); // checked-in
        sql.append("OR (TIMESTAMP(start_time) >= TIMESTAMP(:startDateTime) AND TIMESTAMP(start_time) <= TIMESTAMP(:endDateTime)) ");
        sql.append("OR (DATE_ADD(end_time, INTERVAL 1 HOUR) < now() AND TIMESTAMP(:startDateTime) <= TIMESTAMP(start_time) AND TIMESTAMP(:endDateTime) >= TIMESTAMP(start_time))"); // missed check-out
        sql.append("OR (status = 'CHECKED_OUT' ");
        sql.append("AND TIMESTAMP(end_time) >= TIMESTAMP(:startDateTime) AND TIMESTAMP(end_time) <= TIMESTAMP(:endDateTime)");
        sql.append(") ");
      sql.append(") ");
    // }

    Query query = nativeQuery(sql.toString());
    query.setParameter("businessId", request.getBusinessId());

    if (!StringUtils.isEmpty(request.getSearchKey())) {
      query.setParameter("searchKey", request.getSearchKey());
    } 

    if (!StringUtils.isEmpty(request.getStartDateTime()) && !StringUtils.isEmpty(request.getEndDateTime())) {
      query.setParameter("startDateTime", CommonUtils.convertStringToTimestampUTC(request.getStartDateTime()));
      query.setParameter("endDateTime", CommonUtils.convertStringToTimestampUTC(request.getEndDateTime()));
    }

    if (!StringUtils.isEmpty(request.getStartDateTime())) {
      Integer offset = CommonUtils.getOffset(request.getStartDateTime());
      query.setParameter("offset", offset);
    }

    List<Object[]> result = query.getResultList();
    if (result != null && result.size() > 0) {
      return ((BigInteger) result.get(0)[0]).longValue();
    } else {
      return 0L;
    }
  }

  public Query createQuery(BookingFilterRequest request, boolean isCounting) {
    List<Long> bookingIds = new ArrayList<>();

    ///// FIRST QUERY
    StringBuilder sql1 = new StringBuilder();
    sql1.append("SELECT DISTINCT(b.id) FROM bookings b ");
    sql1.append("JOIN bookings_has_customers bc ON bc.bookings_id = b.id ");
    sql1.append("JOIN customers c ON c.id = bc.customers_id AND c.deleted_at IS NULL ");

    sql1.append(getFilterQuery(request));

    Query query1 = nativeQuery(sql1.toString());
    query1.setParameter("businessId", request.getBusinessId());

    if (!StringUtils.isEmpty(request.getSearchKey())) {
      query1.setParameter("searchKey", request.getSearchKey());
    } else {
      if (!StringUtils.isEmpty(request.getStartDateTime()) && !StringUtils.isEmpty(request.getEndDateTime())) {
        query1.setParameter("startDateTime", CommonUtils.convertStringToTimestampUTC(request.getStartDateTime()));
        query1.setParameter("endDateTime", CommonUtils.convertStringToTimestampUTC(request.getEndDateTime()));
      }
    }

    if (!StringUtils.isEmpty(request.getStartDateTime())) {
      Integer offset = CommonUtils.getOffset(request.getStartDateTime());
      query1.setParameter("offset", offset);
    }

    for (Object obj : query1.getResultList()) {
      bookingIds.add(Long.parseLong(String.valueOf(obj)));
    }
    
    if (bookingIds.isEmpty()) {
      return null;
    }
    
    ///// SECOND QUERY

    StringBuilder sql = new StringBuilder();
    if (!isCounting) {
      sql.append("SELECT DISTINCT(b) FROM Booking b ");
    } else {
      sql.append("SELECT COUNT(DISTINCT b.id) FROM Booking b ");
    }

    sql.append("WHERE b.businessId = :businessId ");

    if (!CollectionUtils.isEmpty(bookingIds)) {
      sql.append("AND b.id IN :bookingIds ");
    }

    if (!isCounting) {
      sql.append("ORDER BY b.startTime ");
      if (request.getSort() != null) {
        sql.append(request.getSort() + " ");
      } else {
        sql.append("ASC ");
      }
    }

    Query query = query(sql.toString());
    query.setParameter("businessId", request.getBusinessId());

    if (!CollectionUtils.isEmpty(bookingIds)) {
      query.setParameter("bookingIds", bookingIds);
    }

    if (request.getSize() != null && request.getPage() != null && !isCounting) {
      query.setFirstResult(request.getSize() * request.getPage());
      query.setMaxResults(request.getSize());
    }

    return query;
  }

  private String getFilterQuery(BookingFilterRequest request) {

    StringBuilder sql1 = new StringBuilder();
    sql1.append("WHERE b.business_id = :businessId ");

    if (!StringUtils.isEmpty(request.getStartDateTime())) {
      sql1.append("AND :offset = :offset ");
    }

    if (!StringUtils.isEmpty(request.getSearchKey())) {
      sql1.append("AND ( ");
      sql1.append("b.booking_number LIKE LOWER(CONCAT('%', REPLACE(:searchKey, '%', '\\%'),'%')) ");
      sql1.append("OR LOWER(c.email) LIKE LOWER(CONCAT('%', REPLACE(:searchKey, '%', '\\%'),'%')) ");
      sql1.append("OR ");
      sql1.append("LOWER(CONCAT(c.first_name, ' ', c.last_name)) LIKE LOWER(CONCAT('%', REPLACE(:searchKey, '%', '\\%'),'%')) ");
      sql1.append(") ");
    } else {
      if (!StringUtils.isEmpty(request.getStartDateTime()) && !StringUtils.isEmpty(request.getEndDateTime())) {
        sql1.append("AND (");
          // cancelled and others
          sql1.append("(b.status IN ('CANCELLED', 'OTHERS', 'CONFIRMED', 'CHECKED_OUT') ");
            sql1.append("AND ( " +
                            "(TIMESTAMP(b.start_time) >= TIMESTAMP(:startDateTime) AND TIMESTAMP(b.start_time) <= TIMESTAMP(:endDateTime)) ");
                sql1.append("OR " +
                            "(TIMESTAMP(b.end_time) >= TIMESTAMP(:startDateTime) AND TIMESTAMP(b.end_time) <= TIMESTAMP(:endDateTime)) ");
            sql1.append(")) ");
          // checked in within the current date range
          // sql1.append("OR (b.status = 'CONFIRMED' ");
          //   sql1.append("AND ((TIMESTAMP(b.start_time) >= TIMESTAMP(:startDateTime) ");
          //   sql1.append("AND TIMESTAMP(b.start_time) <= TIMESTAMP(:endDateTime)) "); // to be checked in
          //   sql1.append("OR (DATE_ADD(b.start_time, INTERVAL 1 HOUR) < now() AND TIMESTAMP(:endDateTime) <= TIMESTAMP(b.end_time) ) "); // missed check-in
          // sql1.append(")) ");
          // to be checked out within the current date range
          sql1.append("OR (b.status = 'CHECKED_IN' ");
            sql1.append("AND (");
              sql1.append("(TIMESTAMP(b.end_time) >= TIMESTAMP(:startDateTime) AND TIMESTAMP(b.end_time) <= TIMESTAMP(:endDateTime))"); // checked-in
              sql1.append("OR (TIMESTAMP(b.start_time) >= TIMESTAMP(:startDateTime) AND TIMESTAMP(b.start_time) <= TIMESTAMP(:endDateTime)) ");
              sql1.append("OR (DATE_ADD(b.end_time, INTERVAL 1 HOUR) < now() AND TIMESTAMP(:startDateTime) <= TIMESTAMP(b.start_time) AND TIMESTAMP(:endDateTime) >= TIMESTAMP(b.start_time))"); // missed check-out
            sql1.append(") ");
          sql1.append(") ");
          // checked out on the current date range
          // sql1.append("OR (b.status = 'CHECKED_OUT' ");
          // sql1.append("AND TIMESTAMP(b.end_time) >= TIMESTAMP(:startDateTime) AND TIMESTAMP(b.end_time) <= TIMESTAMP(:endDateTime)");
          // sql1.append(") ");
        sql1.append(") ");

        if (request.getBookingStatus() != null && request.getBookingStatus().size() > 0) {
          sql1.append("AND ( ");
          boolean addOr = false;
          if (request.getBookingStatus().contains(BookingStatus.CONFIRMED.name()) ) {
            sql1.append("(b.status = 'CONFIRMED' ");
            sql1.append("AND DATE(DATE_ADD(b.start_time, INTERVAL :offset HOUR)) > DATE(DATE_ADD(now(), INTERVAL :offset HOUR)) ");
            sql1.append(") ");
            addOr = true;
          }
    
          if (request.getBookingStatus().contains(BookingStatus.TO_BE_CHECKED_IN.name()) ) {
            if (addOr) {
              sql1.append("OR ");
            }
            sql1.append("(b.status = 'CONFIRMED' ");
            sql1.append("AND DATE(DATE_ADD(b.start_time, INTERVAL :offset HOUR)) = DATE(DATE_ADD(now(), INTERVAL :offset HOUR)) ");
            sql1.append("AND now() <= DATE_ADD(TIMESTAMP(b.start_time), INTERVAL 1 HOUR) ");
            sql1.append(") ");
            addOr = true;
          }
    
          if (request.getBookingStatus().contains(BookingStatus.MISSED_CHECK_IN.name()) ) {
            if (addOr) {
              sql1.append("OR ");
            }
            sql1.append("(b.status = 'CONFIRMED' ");
            sql1.append("AND DATE_ADD(b.start_time, INTERVAL 1 HOUR) < now() ");
            sql1.append(") ");
            addOr = true;
          }
    
          if (request.getBookingStatus().contains(BookingStatus.CHECKED_IN.name()) ) {
            if (addOr) {
              sql1.append("OR ");
            }
            sql1.append("(b.status = 'CHECKED_IN' ");
            sql1.append("AND DATE(DATE_ADD(now(), INTERVAL :offset HOUR)) != DATE(DATE_ADD(b.end_time, INTERVAL :offset HOUR)) ");
            sql1.append("AND now() <= DATE_ADD(b.end_time, INTERVAL 1 HOUR)) ");
            addOr = true;
          }
    
          if (request.getBookingStatus().contains(BookingStatus.TO_BE_CHECKED_OUT.name()) ) {
            if (addOr) {
              sql1.append("OR ");
            }
            sql1.append("(b.status = 'CHECKED_IN' ");
            sql1.append("AND DATE(DATE_ADD(now(), INTERVAL :offset HOUR)) = DATE(DATE_ADD(b.end_time, INTERVAL :offset HOUR)) ");
            sql1.append("AND now() <= DATE_ADD(b.end_time, INTERVAL 1 HOUR)");
            sql1.append(") ");
            addOr = true;
          }
    
          if (request.getBookingStatus().contains(BookingStatus.MISSED_CHECK_OUT.name()) ) {
            if (addOr) {
              sql1.append("OR ");
            }
            sql1.append("(b.status = 'CHECKED_IN' ");
            sql1.append("AND now() > DATE_ADD(b.end_time, INTERVAL 1 HOUR)  ");
            sql1.append(") ");
            addOr = true;
          }
    
          if (request.getBookingStatus().contains(BookingStatus.CANCELLED.name())) {
            if (addOr) {
              sql1.append("OR ");
            }
            sql1.append("b.status = 'CANCELLED' ");
            addOr = true;
          }

          if (request.getBookingStatus().contains(BookingStatus.CHECKED_OUT.name()) ) {
            if (addOr) {
              sql1.append("OR ");
            }
            sql1.append("(b.status = 'CHECKED_OUT' ");
            sql1.append("AND (" );
            sql1.append("TIMESTAMP(b.end_time) >= TIMESTAMP(:startDateTime) AND TIMESTAMP(b.end_time) <= TIMESTAMP(:endDateTime) ");
            sql1.append("OR ");
            sql1.append("TIMESTAMP(b.start_time) >= TIMESTAMP(:startDateTime) AND TIMESTAMP(b.start_time) <= TIMESTAMP(:endDateTime) ");
            sql1.append(")) ");
            addOr = true;
          }
    
          sql1.append(") ");
        }
      }
  
    }

    

    // log.error("SQL 1: ");
    // log.error(sql1.toString());
    return sql1.toString();
  }
}
