package com.maquetech.application.models.reservation;

import com.maquetech.application.enums.reservation.SituationEnum;
import com.maquetech.application.helpers.DateHelper;
import io.micrometer.common.util.StringUtils;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationModel {

    private Long id;

    private String message;
    private String userName;

    private Date bookingEndDate;

    private Date bookingStartDate;

    private SituationEnum situation;

    private List<ReservationMaterialModel> materialList;

    public String getSituationDisplay() {
        return this.situation.getDescription();
    }

    public boolean isCanceled() {
        return SituationEnum.CANCELED.equals(this.situation);
    }

    public boolean isPending() {
        return SituationEnum.PENDING.equals(this.situation);
    }

    public boolean isInProgress() {
        return SituationEnum.IN_PROGRESS.equals(this.situation);
    }

    public boolean isFinished() {
        return SituationEnum.FINISHED.equals(this.situation);
    }

    public boolean isApproved() {
        return SituationEnum.APPROVED.equals(this.situation);
    }

    public String getStartDateDisplay() {
        return DateHelper.displayDate(this.getBookingStartDate());
    }

    public String getEndDateDisplay() {
        return DateHelper.displayDate(this.getBookingEndDate());
    }

    public String getStartHourDisplay() {
        return DateHelper.displayHour(this.getBookingStartDate());
    }

    public String getEndHourDisplay() {
        return DateHelper.displayHour(this.getBookingEndDate());
    }

    public String getMessage() {
        return StringUtils.isNotBlank(message) ? message : "";
    }
}
