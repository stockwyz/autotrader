package com.mcwilliamauto.modules.appointment.mail;

/**
 * 预约相关邮件的 HTML 模板(纯 Java，不依赖 Spring，方便单独预览)。
 * 邮件客户端对 CSS 支持很差，所以全部用 table 布局 + 行内样式。
 */
public final class AppointmentEmailTemplates {

    private AppointmentEmailTemplates() {}

    /** 店铺信息，用于邮件抬头/页脚；primaryColor 跟随后台选的网站风格 */
    public record Dealer(String name, String address, String phone, String primaryColor) {}

    /** 预约信息(已格式化好的展示文本) */
    public record Appt(String apptNo, String typeName, String dateText, String timeText, String detail,
                       String customerName, String phone, String email, String vehicle) {}

    public record Mail(String subject, String html) {}

    private static final String INK = "#14233A";
    private static final String MUTED = "#5B6B80";
    private static final String LINE = "#DCE4EE";

    // ---------------------------------------------------------------
    // 1. 客户提交预约后收到的邮件：预约信息 + 确认/取消按钮
    // ---------------------------------------------------------------
    public static Mail customerReceived(Dealer d, Appt a, String confirmUrl, String cancelUrl) {
        String body = paragraph("Hi " + esc(a.customerName()) + ", we have received your appointment request. "
                + "Please review the details below and let us know whether you can make it.")
                + detailsTable(a, false)
                + buttons(d, confirmUrl, cancelUrl)
                + note("Clicking a button above lets us know your response. Our team will review it and send you a final "
                + "confirmation by email. If you need to change the date or time, please call us"
                + (blank(d.phone()) ? "." : " at " + esc(d.phone()) + "."));
        return new Mail("Please confirm your appointment – " + a.dateText() + ", " + a.timeText(),
                layout(d, "Confirm your appointment", "Appointment request", body));
    }

    // ---------------------------------------------------------------
    // 2. 客户在邮件里点了确认/取消 → 发给店里
    // ---------------------------------------------------------------
    public static Mail dealerResponse(Dealer d, Appt a, boolean confirmed, String adminUrl) {
        String verb = confirmed ? "confirmed" : "cancelled";
        String body = paragraph("<b>" + esc(a.customerName()) + "</b> has <b>" + verb + "</b> this appointment from the email link.")
                + detailsTable(a, true)
                + button(adminUrl, "Open Appointments in Admin", d.primaryColor(), "#FFFFFF", d.primaryColor())
                + note("This is the customer's own response only. The appointment status does not change until you confirm it "
                + "in the admin panel (Appointments → Status).");
        return new Mail("Customer " + verb + " – " + a.customerName() + ", " + a.dateText() + " " + a.timeText(),
                layout(d, "Customer " + verb + " the appointment", confirmed ? "Customer confirmed" : "Customer cancelled", body));
    }

    // ---------------------------------------------------------------
    // 3. 管理员人工确认改状态后 → 发给客户 (status: 1=确认 2=完成 3=取消)
    // ---------------------------------------------------------------
    public static Mail customerStatus(Dealer d, Appt a, int status, String rebookUrl) {
        String subject, title, eyebrow, intro, extra = "";
        switch (status) {
            case 1 -> {
                subject = "Your appointment is confirmed – " + a.dateText() + ", " + a.timeText();
                title = "Your appointment is confirmed";
                eyebrow = "Confirmed";
                intro = "Good news, " + esc(a.customerName()) + "! Your appointment is confirmed. We look forward to seeing you.";
                extra = note("Need to change something? Please call us"
                        + (blank(d.phone()) ? "" : " at " + esc(d.phone())) + " as early as you can.");
            }
            case 2 -> {
                subject = "Your service is complete – thank you";
                title = "Your service is complete";
                eyebrow = "Completed";
                intro = "Hi " + esc(a.customerName()) + ", your appointment has been marked as completed. "
                        + "Thank you for choosing " + esc(d.name()) + ". We appreciate your business.";
            }
            case 3 -> {
                subject = "Your appointment has been cancelled – " + a.dateText() + ", " + a.timeText();
                title = "Your appointment has been cancelled";
                eyebrow = "Cancelled";
                intro = "Hi " + esc(a.customerName()) + ", your appointment has been cancelled. "
                        + "If this was not expected, or you would like to book a new time, you are always welcome.";
                extra = button(rebookUrl, "Book a new appointment", d.primaryColor(), "#FFFFFF", d.primaryColor());
            }
            default -> throw new IllegalArgumentException("Unsupported status: " + status);
        }
        String body = paragraph(intro) + detailsTable(a, false) + extra;
        return new Mail(subject, layout(d, title, eyebrow, body));
    }

    // ---------------------------------------------------------------
    // 公共片段
    // ---------------------------------------------------------------
    private static String layout(Dealer d, String title, String eyebrow, String bodyHtml) {
        String c = d.primaryColor();
        StringBuilder sb = new StringBuilder();
        sb.append("<!doctype html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">")
          .append("<title>").append(esc(title)).append("</title></head>")
          .append("<body style=\"margin:0;padding:0;background:#F2F6FA;\">")
          .append("<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#F2F6FA;padding:24px 12px;\"><tr><td align=\"center\">")
          .append("<table role=\"presentation\" width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;max-width:600px;background:#FFFFFF;border:1px solid ").append(LINE).append(";border-radius:14px;overflow:hidden;font-family:Helvetica,Arial,sans-serif;color:").append(INK).append(";\">")
          // 抬头
          .append("<tr><td style=\"background:").append(c).append(";padding:22px 32px;\">")
          .append("<span style=\"font-size:20px;font-weight:800;letter-spacing:.04em;text-transform:uppercase;color:#FFFFFF;\">").append(esc(d.name())).append("</span>")
          .append("</td></tr>")
          // 标题
          .append("<tr><td style=\"padding:32px 32px 8px;\">")
          .append("<div style=\"font-size:12px;font-weight:800;letter-spacing:.14em;text-transform:uppercase;color:").append(c).append(";margin-bottom:10px;\">").append(esc(eyebrow)).append("</div>")
          .append("<div style=\"font-size:26px;line-height:1.2;font-weight:800;color:").append(INK).append(";\">").append(esc(title)).append("</div>")
          .append("</td></tr>")
          // 正文
          .append("<tr><td style=\"padding:8px 32px 28px;\">").append(bodyHtml).append("</td></tr>")
          // 页脚
          .append("<tr><td style=\"background:#F2F6FA;border-top:1px solid ").append(LINE).append(";padding:18px 32px;font-size:13px;line-height:1.6;color:").append(MUTED).append(";\">")
          .append("<b style=\"color:").append(INK).append(";\">").append(esc(d.name())).append("</b>");
        if (!blank(d.address())) sb.append("<br>").append(esc(d.address()));
        if (!blank(d.phone())) sb.append("<br>Tel: ").append(esc(d.phone()));
        sb.append("</td></tr></table></td></tr></table></body></html>");
        return sb.toString();
    }

    private static String paragraph(String html) {
        return "<p style=\"margin:16px 0 20px;font-size:16px;line-height:1.6;color:#2A3A52;\">" + html + "</p>";
    }

    private static String note(String text) {
        return "<p style=\"margin:20px 0 0;font-size:13px;line-height:1.6;color:" + MUTED + ";\">" + text + "</p>";
    }

    private static String detailsTable(Appt a, boolean includeContact) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"border:1px solid ").append(LINE)
          .append(";border-radius:10px;background:#F7F9FC;\"><tr><td style=\"padding:6px 20px;\">")
          .append("<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">");
        if (!blank(a.apptNo())) row(sb, "Appointment No.", esc(a.apptNo()));
        row(sb, "Service", esc(a.typeName()));
        row(sb, "Date", esc(a.dateText()));
        row(sb, "Time", esc(a.timeText()));
        if (!blank(a.vehicle())) row(sb, "Vehicle", esc(a.vehicle()));
        if (!blank(a.detail())) row(sb, "Detail", esc(a.detail()).replace("\n", "<br>"));
        if (includeContact) {
            row(sb, "Customer", esc(a.customerName()));
            row(sb, "Phone", esc(a.phone()));
            if (!blank(a.email())) row(sb, "Email", esc(a.email()));
        }
        sb.append("</table></td></tr></table>");
        return sb.toString();
    }

    private static void row(StringBuilder sb, String label, String valueHtml) {
        sb.append("<tr><td style=\"padding:11px 0;border-bottom:1px solid ").append(LINE)
          .append(";width:130px;font-size:13px;color:").append(MUTED).append(";vertical-align:top;\">").append(label)
          .append("</td><td style=\"padding:11px 0;border-bottom:1px solid ").append(LINE)
          .append(";font-size:15px;font-weight:700;color:").append(INK).append(";vertical-align:top;\">").append(valueHtml).append("</td></tr>");
    }

    /** 两个并排按钮：确认(实心) + 取消(描边)；窄屏下 table 会自然折行 */
    private static String buttons(Dealer d, String confirmUrl, String cancelUrl) {
        return "<table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:26px 0 0;\"><tr>"
                + "<td style=\"padding:0 12px 12px 0;\">" + buttonLink(confirmUrl, "Confirm appointment", d.primaryColor(), "#FFFFFF", d.primaryColor()) + "</td>"
                + "<td style=\"padding:0 0 12px 0;\">" + buttonLink(cancelUrl, "Cancel appointment", "#FFFFFF", "#B42318", "#D92D20") + "</td>"
                + "</tr></table>";
    }

    private static String button(String url, String label, String bg, String fg, String border) {
        return "<div style=\"margin:26px 0 0;\">" + buttonLink(url, label, bg, fg, border) + "</div>";
    }

    private static String buttonLink(String url, String label, String bg, String fg, String border) {
        return "<a href=\"" + esc(url) + "\" target=\"_blank\" style=\"display:inline-block;padding:14px 26px;border-radius:8px;border:2px solid "
                + border + ";background:" + bg + ";color:" + fg + ";font-size:14px;font-weight:800;letter-spacing:.06em;text-transform:uppercase;text-decoration:none;\">"
                + esc(label) + "</a>";
    }

    private static boolean blank(String s) { return s == null || s.isBlank(); }

    /** 最基本的 HTML 转义，防止客户填写的内容把邮件版式弄乱或注入标签 */
    static String esc(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (char ch : s.toCharArray()) {
            switch (ch) {
                case '&' -> sb.append("&amp;");
                case '<' -> sb.append("&lt;");
                case '>' -> sb.append("&gt;");
                case '"' -> sb.append("&quot;");
                case '\'' -> sb.append("&#39;");
                default -> sb.append(ch);
            }
        }
        return sb.toString();
    }
}
