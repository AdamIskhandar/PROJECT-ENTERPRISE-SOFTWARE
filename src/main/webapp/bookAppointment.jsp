<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.ScheduleOption" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>

<%
    List<ScheduleOption> schedules =
            (List<ScheduleOption>)
                    request.getAttribute("schedules");

    String patientId =
            (String)
                    request.getAttribute("patientId");

    String source =
            (String)
                    request.getAttribute("source");

    String error =
            (String)
                    request.getAttribute("error");


    if (patientId == null) {

        patientId =
                request.getParameter(
                        "patientId"
                );
    }


    if (source == null ||
            source.trim().isEmpty()) {

        source =
                request.getParameter(
                        "source"
                );
    }


    if (source == null ||
            source.trim().isEmpty()) {

        source = "patient";
    }
%>


<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width,
          initial-scale=1.0">

    <title>
        Book Appointment - ClinicCare
    </title>


    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/clinic.css?v=5">


    <style>

        .booking-container {

            width: min(850px, 94%);
            margin: 40px auto;

        }


        .booking-card {

            background: white;
            border-radius: 18px;

            padding: 32px;

            box-shadow:
                    0 12px 35px
                    rgba(0, 0, 0, 0.08);

        }


        .booking-header {

            margin-bottom: 28px;

        }


        .booking-header h1 {

            margin-bottom: 8px;

        }


        .booking-header p {

            color: #6b7280;
            margin: 0;

        }


        .form-group {

            margin-bottom: 22px;

        }


        .form-group label {

            display: block;

            font-weight: 600;

            margin-bottom: 8px;

        }


        .form-group input,
        .form-group select,
        .form-group textarea {

            width: 100%;

            box-sizing: border-box;

            padding: 12px 14px;

            border: 1px solid #d1d5db;

            border-radius: 10px;

            font-size: 15px;

            background: white;

        }


        .form-group textarea {

            resize: vertical;

            min-height: 110px;

        }


        .form-group input:focus,
        .form-group select:focus,
        .form-group textarea:focus {

            outline: none;

            border-color: #2563eb;

            box-shadow:
                    0 0 0 3px
                    rgba(37, 99, 235, 0.10);

        }


        .calculation-box {

            margin-top: 22px;
            margin-bottom: 25px;

            padding: 22px;

            border-radius: 14px;

            background: #f8fafc;

            border: 1px solid #e5e7eb;

        }


        .calculation-title {

            font-size: 17px;

            font-weight: 700;

            margin-bottom: 16px;

        }


        .calculation-row {

            display: flex;

            justify-content: space-between;

            gap: 20px;

            margin-bottom: 10px;

        }


        .calculation-label {

            color: #6b7280;

        }


        .calculation-value {

            font-weight: 600;

            text-align: right;

        }


        .rate-breakdown {

            margin-top: 16px;

            padding-top: 16px;

            border-top: 1px solid #e5e7eb;

        }


        .rate-line {

            margin-bottom: 8px;

        }


        .total-row {

            margin-top: 16px;

            padding-top: 16px;

            border-top: 2px solid #e5e7eb;

            display: flex;

            justify-content: space-between;

            align-items: center;

            font-size: 18px;

            font-weight: 700;

        }


        .total-price {

            font-size: 22px;

        }


        .button-row {

            display: flex;

            gap: 12px;

            margin-top: 25px;

            flex-wrap: wrap;

        }


        .primary-button {

            border: none;

            border-radius: 10px;

            padding: 12px 22px;

            font-size: 15px;

            font-weight: 600;

            cursor: pointer;

            background: #2563eb;

            color: white;

        }


        .primary-button:hover {

            opacity: 0.9;

        }


        .cancel-button {

            text-decoration: none;

            border-radius: 10px;

            padding: 12px 22px;

            font-size: 15px;

            font-weight: 600;

            background: #f3f4f6;

            color: #111827;

        }


        .error-box {

            padding: 14px 16px;

            border-radius: 10px;

            margin-bottom: 20px;

            background: #fef2f2;

            color: #b91c1c;

            border: 1px solid #fecaca;

        }


        .empty-box {

            padding: 20px;

            text-align: center;

            border-radius: 12px;

            background: #f8fafc;

            color: #6b7280;

            margin-bottom: 20px;

        }


        .rate-note {

            margin-top: 14px;

            font-size: 13px;

            color: #6b7280;

            line-height: 1.6;

        }


        @media(max-width: 600px) {

            .booking-card {

                padding: 22px;

            }


            .calculation-row,
            .total-row {

                flex-direction: column;

                gap: 4px;

            }


            .calculation-value {

                text-align: left;

            }

        }

    </style>

</head>


<body>


<div class="booking-container">


    <div class="booking-card">


        <!-- =========================================
             HEADER
             ========================================= -->

        <div class="booking-header">

            <span class="eyebrow">
                Patient Appointment
            </span>


            <h1>
                Book Appointment
            </h1>


            <p>
                Choose an available doctor schedule
                and confirm your appointment.
            </p>

        </div>



        <!-- =========================================
             ERROR MESSAGE
             ========================================= -->

        <% if (error != null &&
                !error.trim().isEmpty()) { %>

            <div class="error-box">

                <%= HtmlUtil.escape(error) %>

            </div>

        <% } %>



        <!-- =========================================
             NO SCHEDULE
             ========================================= -->

        <% if (schedules == null ||
                schedules.isEmpty()) { %>


            <div class="empty-box">

                There are currently no
                available doctor schedules.

            </div>


        <% } %>



        <!-- =========================================
             BOOKING FORM
             ========================================= -->

        <form action="<%= request.getContextPath() %>/bookAppointment"
              method="post">


            <!-- =====================================
                 SOURCE
                 ===================================== -->

            <input type="hidden"
                   name="source"
                   value="<%= HtmlUtil.escape(source) %>">


            <!-- =====================================
                 PATIENT ID
                 Mainly used for admin booking.
                 Patient booking should use session ID.
                 ===================================== -->

            <% if (patientId != null &&
                    !patientId.trim().isEmpty()) { %>

                <input type="hidden"
                       name="patientId"
                       value="<%= HtmlUtil.escape(patientId) %>">

            <% } %>



            <!-- =====================================
                 AVAILABLE SCHEDULE DROPDOWN
                 ===================================== -->

            <div class="form-group">

                <label for="scheduleId">
                    Available Doctor Schedule
                </label>


                <select name="scheduleId"
                        id="scheduleId"
                        required
                        onchange="showScheduleDetails()">


                    <option value="">

                        Select an available schedule

                    </option>


                    <% if (schedules != null) {

                        for (ScheduleOption schedule :
                                schedules) {

                    %>


                        <option

                            value="<%= schedule.getScheduleId() %>"


                            data-doctor="<%= HtmlUtil.escape(
                                    schedule.getDoctorName()
                            ) %>"


                            data-specialization="<%= HtmlUtil.escape(
                                    schedule.getSpecialization()
                            ) %>"


                            data-date="<%= schedule.getAvailableDate() %>"


                            data-start="<%= schedule.getStartTime() %>"


                            data-end="<%= schedule.getEndTime() %>"


                            data-rate-type="<%= HtmlUtil.escape(
                                    schedule.getRateType()
                            ) %>"


                            data-rate="<%= schedule.getRateUsed() %>"


                            data-duration="<%= schedule.getDurationHours() %>"


                            data-charge="<%= schedule.getEstimatedCharge() %>"


                            data-morning-rate="<%= schedule.getMorningRate() %>"


                            data-night-rate="<%= schedule.getNightRate() %>"


                            data-morning-hours="<%= schedule.getMorningHours() %>"


                            data-night-hours="<%= schedule.getNightHours() %>"


                            data-morning-charge="<%= schedule.getMorningCharge() %>"


                            data-night-charge="<%= schedule.getNightCharge() %>">


                            <%= schedule.getAvailableDate() %>

                            ·

                            <%= schedule.getStartTime() %>

                            -

                            <%= schedule.getEndTime() %>

                            ·

                            <%= HtmlUtil.escape(
                                    schedule.getDoctorName()
                            ) %>

                            (

                            <%= HtmlUtil.escape(
                                    schedule.getSpecialization()
                            ) %>

                            )

                        </option>


                    <%

                        }

                    }

                    %>


                </select>

            </div>



            <!-- =====================================
                 CHARGE PREVIEW
                 ===================================== -->

            <div class="calculation-box"
                 id="scheduleInfo"
                 style="display:none;">


                <div class="calculation-title">

                    Appointment Charge Preview

                </div>



                <!-- Doctor -->

                <div class="calculation-row">

                    <span class="calculation-label">

                        Doctor

                    </span>


                    <span class="calculation-value"
                          id="doctorText">

                    </span>

                </div>



                <!-- Date -->

                <div class="calculation-row">

                    <span class="calculation-label">

                        Date & Time

                    </span>


                    <span class="calculation-value"
                          id="dateTimeText">

                    </span>

                </div>



                <!-- Rate Type -->

                <div class="calculation-row">

                    <span class="calculation-label">

                        Rate Type

                    </span>


                    <span class="calculation-value"
                          id="rateTypeText">

                    </span>

                </div>



                <!-- Duration -->

                <div class="calculation-row">

                    <span class="calculation-label">

                        Total Duration

                    </span>


                    <span class="calculation-value">

                        <span id="durationText">
                        </span>

                        hour(s)

                    </span>

                </div>



                <!-- =================================
                     MORNING / NIGHT BREAKDOWN
                     ================================= -->

                <div class="rate-breakdown">


                    <div id="morningBreakdown"
                         class="rate-line">

                    </div>


                    <div id="nightBreakdown"
                         class="rate-line">

                    </div>


                </div>



                <!-- =================================
                     TOTAL CHARGE
                     ================================= -->

                <div class="total-row">


                    <span>

                        Total Charge

                    </span>


                    <span class="total-price">

                        RM

                        <span id="chargeText">

                            0.00

                        </span>

                    </span>


                </div>



                <div class="rate-note">

                    Morning rate applies before
                    7:00 PM.

                    Night rate starts from
                    7:00 PM.

                    If an appointment crosses
                    7:00 PM, both rates are
                    calculated separately.

                </div>


            </div>



            <!-- =====================================
                 REASON
                 ===================================== -->

            <div class="form-group">

                <label for="reason">

                    Reason for Appointment

                </label>


                <textarea

                    name="reason"

                    id="reason"

                    maxlength="255"

                    placeholder="Example: Fever, consultation, check-up..."></textarea>

            </div>



            <!-- =====================================
                 BUTTONS
                 ===================================== -->

            <div class="button-row">


                <button type="submit"
                        class="primary-button"

                    <% if (schedules == null ||
                            schedules.isEmpty()) { %>

                        disabled

                    <% } %>>

                    Confirm Booking

                </button>



                <% if ("patient".equalsIgnoreCase(
                        source)) { %>


                    <a class="cancel-button"
                       href="<%= request.getContextPath() %>/myAppointments">

                        Cancel

                    </a>


                <% } else { %>


                    <a class="cancel-button"
                       href="<%= request.getContextPath() %>/manageAppointments">

                        Cancel

                    </a>


                <% } %>


            </div>


        </form>


    </div>


</div>



<!-- =========================================
     JAVASCRIPT
     ========================================= -->

<script>


    function formatMoney(value) {

        const number =
            parseFloat(value || "0");


        return number.toFixed(2);
    }



    function formatHours(value) {

        const number =
            parseFloat(value || "0");


        return number.toFixed(2);
    }



    function showScheduleDetails() {


        const select =
            document.getElementById(
                "scheduleId"
            );


        const info =
            document.getElementById(
                "scheduleInfo"
            );


        if (!select) {

            return;
        }


        const option =
            select.options[
                select.selectedIndex
            ];



        // =====================================
        // NOTHING SELECTED
        // =====================================

        if (!option ||
                !option.value) {


            info.style.display =
                "none";


            return;
        }



        // =====================================
        // DOCTOR
        // =====================================

        document.getElementById(
            "doctorText"
        ).textContent =

            option.dataset.doctor
            + " ("
            + option.dataset.specialization
            + ")";



        // =====================================
        // DATE & TIME
        // =====================================

        document.getElementById(
            "dateTimeText"
        ).textContent =

            option.dataset.date
            + " · "
            + option.dataset.start
            + " - "
            + option.dataset.end;



        // =====================================
        // RATE TYPE
        // =====================================

        document.getElementById(
            "rateTypeText"
        ).textContent =

            option.dataset.rateType;



        // =====================================
        // DURATION
        // =====================================

        document.getElementById(
            "durationText"
        ).textContent =

            formatHours(
                option.dataset.duration
            );



        // =====================================
        // GET RATE INFORMATION
        // =====================================

        const morningHours =
            parseFloat(
                option.dataset.morningHours
                || "0"
            );


        const nightHours =
            parseFloat(
                option.dataset.nightHours
                || "0"
            );


        const morningRate =
            parseFloat(
                option.dataset.morningRate
                || "0"
            );


        const nightRate =
            parseFloat(
                option.dataset.nightRate
                || "0"
            );


        const morningCharge =
            parseFloat(
                option.dataset.morningCharge
                || "0"
            );


        const nightCharge =
            parseFloat(
                option.dataset.nightCharge
                || "0"
            );



        const morningBox =
            document.getElementById(
                "morningBreakdown"
            );


        const nightBox =
            document.getElementById(
                "nightBreakdown"
            );



        // =====================================
        // MORNING RATE
        // =====================================

        if (morningHours > 0) {


            morningBox.innerHTML =

                "<strong>Morning:</strong> "
                + formatHours(morningHours)
                + " hr × RM "
                + formatMoney(morningRate)
                + " = "
                + "<strong>RM "
                + formatMoney(morningCharge)
                + "</strong>";


            morningBox.style.display =
                "block";


        } else {


            morningBox.innerHTML = "";

            morningBox.style.display =
                "none";

        }



        // =====================================
        // NIGHT RATE
        // =====================================

        if (nightHours > 0) {


            nightBox.innerHTML =

                "<strong>Night (from 7:00 PM):</strong> "
                + formatHours(nightHours)
                + " hr × RM "
                + formatMoney(nightRate)
                + " = "
                + "<strong>RM "
                + formatMoney(nightCharge)
                + "</strong>";


            nightBox.style.display =
                "block";


        } else {


            nightBox.innerHTML = "";

            nightBox.style.display =
                "none";

        }



        // =====================================
        // TOTAL
        // =====================================

        document.getElementById(
            "chargeText"
        ).textContent =

            formatMoney(
                option.dataset.charge
            );



        // =====================================
        // SHOW PREVIEW
        // =====================================

        info.style.display =
            "block";

    }



    // =========================================
    // DISPLAY DATA IF ALREADY SELECTED
    // =========================================

    document.addEventListener(
        "DOMContentLoaded",
        function () {

            showScheduleDetails();

        }
    );


</script>


</body>

</html>