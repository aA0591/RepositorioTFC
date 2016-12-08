package com.webratio.units.store.social;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DoodleXMLManager {

    /**
     * @param rootDoc
     * @return poll
     * @throws ParseException
     * @throws DoodleServiceException
     */
    public static DoodlePoll getPollFromDocument(String pollId, String adminKey, Document rootDoc) throws ParseException {

        String name = "";

        GregorianCalendar lastChange = null;

        DoodlePollType type = DoodlePollType.TEXT;
        String title = "";
        String location = "";
        String description = "";

        boolean hidden = false;
        DoodleState state = null;

        DoodleLevel levels = null;
        String initiator_name = "";
        String initiator_userId = "";
        String initiator_eMailAddress = "";
        DoodleInitiator initiator = null;

        List options = new ArrayList();
        List participants = new ArrayList();
        List comments = new ArrayList();

        Element root = rootDoc.getRootElement();

        Iterator it = root.elementIterator();
        while (it.hasNext()) {

            Element element = (Element) it.next();
            name = element.getName();

            if (name.equals("latestChange")) {
                lastChange = (GregorianCalendar) Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                lastChange.setTime(dateFormat.parse(element.getText()));
            }
            if (name.equals("type"))
                type = DoodlePollType.fromValue(element.getText());
            if (name.equals("hidden"))
                hidden = (element.getText()).equals("FALSE");
            if (name.equals("levels")) {
                if (element.getText().equals("2"))
                    levels = DoodleLevel.TWO;
                else
                    levels = DoodleLevel.THREE;
            }
            if (name.equals("state"))
                state = DoodleState.fromValue(element.getText());
            if (name.equals("title"))
                title = element.getText();
            if (name.equals("location"))
                location = element.getText();
            if (name.equals("description")) {
                description = element.getText();
            }
            if (name.equals("initiator")) {
                Iterator it2 = element.elementIterator();
                while (it2.hasNext()) {
                    Element element2 = (Element) it2.next();
                    name = element2.getName();
                    if (name.equals("name"))
                        initiator_name = element2.getText();
                    if (name.equals("userId"))
                        initiator_userId = element2.getText();
                    if (name.equals("eMailAddress"))
                        initiator_eMailAddress = element2.getText();
                }
                if (!initiator_userId.equals("") && !initiator_eMailAddress.equals("")) {
                    initiator = new DoodleInitiator(initiator_name, initiator_userId, initiator_eMailAddress);
                } else if (!initiator_eMailAddress.equals("")) {
                    initiator = new DoodleInitiator(initiator_name, initiator_eMailAddress);
                } else {
                    initiator = new DoodleInitiator(initiator_name);
                }
            }
            // END INITIATOR

            if (name == "options") {
                boolean isfinal = false;
                Iterator it2 = element.elementIterator();
                while (it2.hasNext()) {
                    Element element2 = (Element) it2.next();
                    name = element2.getName();
                    Attribute isfinal_ = element2.attribute("final");
                    if (isfinal_ != null)
                        isfinal = true;
                    else
                        isfinal = false;
                    DoodleOption aux_option = null;
                    if (name == "option") {
                        if (type.equals(DoodlePollType.TEXT)) {
                            aux_option = new DoodleOption(element2.getText());
                            aux_option.setFinal_(isfinal);

                        } else if (type.equals(DoodlePollType.DATE)) {
                            List attributes = element2.attributes();
                            ListIterator ita = attributes.listIterator();

                            while (ita.hasNext()) {
                                Node atr = (Node) ita.next();

                                if ("date".equals(atr.getName())) {
                                    GregorianCalendar date = (GregorianCalendar) Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    date.setTime(dateFormat.parse(atr.getText()));
                                    aux_option = new DoodleOption(date, false, element2.getText());
                                    aux_option.setFinal_(isfinal);

                                } else if ("startDateTime".equals(atr.getName())) {
                                    GregorianCalendar dateStartTime = (GregorianCalendar) Calendar.getInstance();
                                    GregorianCalendar dateEndTime = (GregorianCalendar) Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    dateStartTime.setTime(dateFormat.parse(atr.getText()));
                                    dateEndTime.setTime(dateFormat.parse(element2.attribute("endDateTime").getText()));
                                    aux_option = new DoodleOption(dateStartTime, dateEndTime);
                                    aux_option.setFinal_(isfinal);

                                } else if ("dateTime".equals(atr.getName())) {
                                    GregorianCalendar dateTime = (GregorianCalendar) Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    dateTime.setTime(dateFormat.parse(atr.getText()));
                                    aux_option = new DoodleOption(dateTime, true, null);
                                    aux_option.setFinal_(isfinal);

                                }

                            }

                        }
                        options.add(aux_option);
                    }
                }
            }// END OPTIONS */

            if (name.equals("participants")) {

                String id = null;
                String participantKey = null;
                String name_ = null;
                String userId = null;
                List preferences = new ArrayList();
                Iterator it2 = element.elementIterator();

                while (it2.hasNext()) {
                    Element element2 = (Element) it2.next();
                    name = element2.getName();

                    if (name.equals("participant")) {
                        Iterator it3 = element2.elementIterator();

                        while (it3.hasNext()) {
                            Element element3 = (Element) it3.next();
                            name = element3.getName();

                            if (name.equals("id"))
                                id = element3.getText();
                            if (name.equals("name"))
                                name_ = element3.getText();
                            if (name.equals("participantKey"))
                                participantKey = element3.getText();
                            if (name.equals("userId"))
                                userId = element3.getText();
                            if (name.equals("preferences")) {
                                Integer option = new Integer(0);
                                Iterator it4 = element3.elementIterator();
                                while (it4.hasNext()) {
                                    Element element4 = (Element) it4.next();
                                    name = element4.getName();

                                    if (name.equals("option")) {
                                        String num = element4.getText();
                                        if (num.equals("")) {
                                            option = new Integer(-1);
                                        } else {
                                            option = new Integer(element4.getText());
                                        }

                                    }
                                    preferences.add(option);
                                }

                            }

                        }

                    }
                    DoodleParticipant aux = new DoodleParticipant(id, participantKey, name_, userId, preferences);
                    preferences = new ArrayList();
                    participants.add(aux);
                }

            }
            // END PARTICIPANTS

            if (name.equals("comments")) {
                Iterator it2 = element.elementIterator();

                while (it2.hasNext()) {
                    long id = 0;
                    String who = null;
                    String userId = null;
                    GregorianCalendar when = null;
                    String what = null;

                    Element element2 = (Element) it2.next();
                    name = element2.getName();

                    if (name.equals("comment")) {
                        Iterator it3 = element2.elementIterator();

                        while (it3.hasNext()) {
                            Element element3 = (Element) it3.next();
                            name = element3.getName();

                            if (name.equals("id"))
                                id = Long.parseLong(element3.getText());
                            if (name.equals("who"))
                                who = element3.getText();
                            if (name.equals("userId"))
                                userId = element3.getText();
                            if (name.equals("when")) {
                                when = (GregorianCalendar) Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                when.setTime(dateFormat.parse(element3.getText()));
                            }
                            if (name.equals("what")) {
                                what = element3.getText();
                            }

                        }
                        DoodleComment comment = new DoodleComment(id, who, userId, when, what);
                        comments.add(comment);
                    }
                }
            }// END COMMENTS

        }

        DoodlePoll poll = new DoodlePoll(pollId, adminKey, lastChange, type, hidden, levels, state, title, location, description,
                initiator, options, participants, comments);

        return poll;
    }

    public static DoodleUser getUserFromDocument(Document rootDocument) {

        String userName = "";
        String userId = "";
        String userEmail = "";
        String pollId = "";
        String adminKey = "";
        String pollTitle = "";
        String pollState = "";
        List userPolls = new ArrayList();
        String elementName;
        Element root = rootDocument.getRootElement();

        Iterator it = root.elementIterator();
        while (it.hasNext()) {

            Element element = (Element) it.next();
            elementName = element.getName();

            if (elementName.equals("name")) {
                userName = element.getText();
            } else if (elementName.equals("userId")) {
                userId = element.getText();
            } else if (elementName.equals("eMailAddress")) {
                userEmail = element.getText();
            } else if (elementName.equals("polls")) {
                Iterator itPolls = element.elementIterator();
                while (itPolls.hasNext()) {
                    // Getting the different Polls
                    Element pollsElement = (Element) itPolls.next();
                    if (pollsElement.getName().equals("poll")) {
                        Iterator itPoll = pollsElement.elementIterator();
                        // Get values from the poll
                        while (itPoll.hasNext()) {
                            Element pollElement = (Element) itPoll.next();
                            String pollElementName = pollElement.getName();
                            if (pollElementName.equals("pollId")) {
                                pollId = pollElement.getText();
                            } else if (pollElementName.equals("adminKey")) {
                                adminKey = pollElement.getText();
                            } else if (pollElementName.equals("title")) {
                                pollTitle = pollElement.getText();
                            } else if (pollElementName.equals("state")) {
                                pollState = pollElement.getText();
                            }

                        }
                        userPolls.add(new DoodlePoll(pollId, adminKey, pollTitle, pollState));
                    }
                }
            }

        }
        return new DoodleUser(userName, userEmail, userId, userPolls);
    }

    /**
     * This method creates XML representation of {@link DoodlePoll} object according to poll.xsd
     * 
     * @param poll
     * @return String
     */

    public static String createPollXml(DoodlePoll poll) {

        StringBuffer pollXml = new StringBuffer();
        pollXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        pollXml.append("<poll xmlns=\"http://doodle.com/xsd1\">");
        pollXml.append("<type>" + poll.getType() + "</type>");
        pollXml.append("<hidden>" + poll.isHidden() + "</hidden>");

        try {
            if (poll.getLevels() != null)
                pollXml.append("<levels>" + poll.getLevels().getLevel() + "</levels>");
            else
                pollXml.append("<levels>2</levels>");
        } catch (NullPointerException io) {

        }

        try {
            if (poll.getState().equals(DoodleState.CLOSED))
                pollXml.append("<state>CLOSED</state>");
            else if (poll.getState().equals(DoodleState.OPEN))
                pollXml.append("<state>OPEN</state>");
            else
                pollXml.append("<state>DELETED</state>");
        } catch (NullPointerException io) {

        }

        pollXml.append("<title>" + poll.getTitle() + "</title>");
        try {
            if (poll.getLocation().length() > 0)
                pollXml.append("<location>" + poll.getLocation() + "</location>");
        } catch (NullPointerException io) {

        }
        try {
            if ((poll.getDescription().length() > 0) && !poll.getDescription().equals("null"))
                pollXml.append("<description>" + poll.getDescription() + "</description>");
            else
                pollXml.append("<description/>");

        } catch (NullPointerException io) {
            pollXml.append("<description/>");

        }

        // INITIATOR
        pollXml.append("<initiator>");
        pollXml.append("<name>" + poll.getInitiator().getName() + "</name>");

        try {
            String userId = (String) poll.getInitiator().getUserId();
            String email = (String) poll.getInitiator().geteMailAddress();
            if (userId != null)
                pollXml.append("<userId>" + userId + "</userId>");
            if (email != null)
                pollXml.append("<eMailAddress>" + email + "</eMailAddress>");

        } catch (NullPointerException io) {

        }
        pollXml.append("</initiator>");
        // END INITIATOR
        // OPTIONS
        pollXml.append("<options>");

        if (poll.getType().equals(DoodlePollType.TEXT)) {
            Iterator it = poll.getOptions().listIterator();
            while (it.hasNext()) {
                DoodleOption option = (DoodleOption) it.next();
                if (option.isFinal_())
                    pollXml.append("<option final=" + '"' + "true" + '"' + ">" + option.getValue() + "</option>");
                else
                    pollXml.append("<option>" + option.getValue() + "</option>");
            }
        }
        if (poll.getType().equals(DoodlePollType.DATE)) {
            Iterator it = poll.getOptions().listIterator();
            while (it.hasNext()) {
                DoodleOption option = (DoodleOption) it.next();
                if ((option.getStartDateTime() != null) || (option.getDateTime() != null) || (option.getDate() != null)) {
                    if (option.isFinal_())
                        pollXml.append("<option final=" + '"' + "true" + '"');
                    else
                        pollXml.append("<option");

                    if (option.getStartDateTime() != null && option.getEndDateTime() != null) {
                        pollXml.append(" startDateTime=" + '"' + parseDateTime(option.getStartDateTime(), true) + '"' + " ");
                        pollXml.append("endDateTime=" + '"' + parseDateTime(option.getEndDateTime(), true) + '"' + "/>");
                    } else if (option.getDateTime() != null)
                        pollXml.append(" dateTime=" + '"' + parseDateTime(option.getDateTime(), true) + '"' + "/>");
                    else if (option.getDate() != null) {
                        pollXml.append(" date=" + '"' + parseDateTime(option.getDate(), false) + '"');
                        if (option.getValue() != null)
                            pollXml.append(">" + option.getValue() + "</option>");
                        else
                            pollXml.append("/>");
                    }
                }
            }
        }
        pollXml.append("</options>");
        // END OPTIONS

        pollXml.append("</poll>");
        return pollXml.toString();
    }

    /**
     * This method creates XML representation of object according to participant.xsd
     * 
     * @param participant
     * @return
     */
    public static String createVoteXml(DoodleParticipant participant) {
        StringBuffer voteXml = new StringBuffer();
        voteXml.append("<participant xmlns=\"http://doodle.com/xsd1\">");
        voteXml.append("<name>" + participant.getName() + "</name>");
        voteXml.append("<preferences>");
        for (int i = 0; i < participant.getPreferences().size(); i++) {
            voteXml.append("<option>" + participant.getPreferences().get(i) + "</option>");
        }

        voteXml.append("</preferences></participant>");
        return voteXml.toString();

    }

    /**
     * This method creates XML representation of object according to comment.xsd
     * 
     * @param comment
     * @return
     */

    public static String createCommentsXml(DoodleComment comment) {
        StringBuffer commentXml = new StringBuffer();
        commentXml.append("<comment xmlns=\"http://doodle.com/xsd1\">");
        commentXml.append("<who>" + comment.getWho() + "</who>");
        commentXml.append("<what>" + comment.getWhat() + "</what>");
        commentXml.append("</comment>");
        return commentXml.toString();

    }

    /**
     * This method creates String with format of Doodle
     * 
     * @param data
     * @param dateTimeV
     *            : to see if the format is date and time, or just data
     * @return String with format datetime or data
     */
    public static String parseDateTime(GregorianCalendar data, boolean dateTimeV) {
        // 2011-04-19T16:58:08+02:00
        String day;
        Integer year, month, date, hour, minute, second;
        year = new Integer(data.get(GregorianCalendar.YEAR));
        month = new Integer(data.get(GregorianCalendar.MONTH) + 1);
        date = new Integer(data.get(GregorianCalendar.DATE));
        hour = new Integer(data.get(GregorianCalendar.HOUR_OF_DAY));
        minute = new Integer(data.get(GregorianCalendar.MINUTE));
        second = new Integer(data.get(GregorianCalendar.SECOND));

        if (dateTimeV)
            day = year + "-" + addZeroParse(month) + "-" + addZeroParse(date) + "T" + addZeroParse(hour) + ":" + addZeroParse(minute)
                    + ":" + addZeroParse(second);
        else
            day = year + "-" + addZeroParse(month) + "-" + addZeroParse(date);

        return day;
    }

    /**
     * This method is auxiliary, if the number has only one digit, it returns a number with two digits (for values ​​less than 10)
     * 
     * @param number
     * @return String with two digits
     */
    public static String addZeroParse(Integer number) {
        String aux = "0";
        String cad;
        if (number.intValue() > 9)
            cad = number.toString();
        else
            cad = aux.concat(number.toString());
        return cad;
    }

}