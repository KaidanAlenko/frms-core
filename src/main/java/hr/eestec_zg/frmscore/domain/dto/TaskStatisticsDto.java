package hr.eestec_zg.frmscore.domain.dto;

public class TaskStatisticsDto {
    private Long successful;
    private Long unsuccessful;
    private Long numberOfEvents;

    private TaskStatisticsDto() {
    }

    public TaskStatisticsDto(Long successful, Long unsuccessful, Long numberOfEvents) {
        this.successful = successful;
        this.unsuccessful = unsuccessful;
        this.numberOfEvents = numberOfEvents;
    }

    public Long getSuccessful() {
        return successful;
    }

    public Long getUnsuccessful() {
        return unsuccessful;
    }

    public Long getNumberOfEvents() {
        return numberOfEvents;
    }
}
