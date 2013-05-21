//
// Nexus Chat Demo - demonstrates Nexus with some chattery
// http://github.com/threerings/nexus/blob/master/LICENSE

package nexus.chat.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import react.Slot;
import react.UnitSlot;

import com.samskivert.swing.GroupLayout;
import com.threerings.nexus.client.Subscriber;

import nexus.chat.distrib.ChatObject;
import nexus.chat.distrib.RoomObject;

/**
 * Displays the main chat interface, once connected.
 */
public class ChatPanel extends JPanel
{
    public ChatPanel (ChatContext ctx, ChatObject chatobj) {
        _ctx  = ctx;
        _chatobj = chatobj;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // add the list of rooms on the right
        JPanel left = GroupLayout.makeVStretchBox(5);
        left.add(new JLabel("Rooms"), GroupLayout.FIXED);
        left.add(_rooms = new JList());
        JButton join = new JButton("Join");
        left.add(join, GroupLayout.FIXED);
        add(left, BorderLayout.EAST);

        join.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                String room = (String)_rooms.getSelectedValue();
                if (room != null) {
                    joinRoom(room);
                }
            }
        });

        JPanel main = GroupLayout.makeVStretchBox(5);
        add(main, BorderLayout.CENTER);

        // add a nickname configuration and room creation UI up top
        final JTextField nickname = new JTextField();
        JButton upnick = new JButton("Update");
        ActionListener onUpNick = new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                updateNickname(nickname.getText().trim());
            }
        };
        nickname.addActionListener(onUpNick);
        upnick.addActionListener(onUpNick);

        final JTextField roomname = new JTextField();
        JButton newroom = new JButton("Create");
        ActionListener creator = new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                createRoom(roomname.getText().trim());
                roomname.setText("");
            }
        };
        roomname.addActionListener(creator);
        newroom.addActionListener(creator);

        JPanel toprow = GroupLayout.makeHStretchBox(5);
        main.add(toprow, GroupLayout.FIXED);
        toprow.add(new JLabel("Nickname:"), GroupLayout.FIXED);
        toprow.add(nickname);
        toprow.add(upnick, GroupLayout.FIXED);
        toprow.add(new JLabel("Create room:"), GroupLayout.FIXED);
        toprow.add(roomname);
        toprow.add(newroom, GroupLayout.FIXED);

        // add a label displaying the current room name
        main.add(_curRoom = new JLabel("Room: <none>"), GroupLayout.FIXED);

        // add the main chat display
        main.add(_chat = new JTextArea());
        _chat.setEditable(false);

        // finally add a UI for entering a chat message
        JPanel chatrow = GroupLayout.makeHStretchBox(5);
        chatrow.add(_entry = new JTextField());
        _entry.setEnabled(false);
        final JButton send = new JButton("Send");
        chatrow.add(send, GroupLayout.FIXED);
        main.add(chatrow, GroupLayout.FIXED);

        ActionListener sender = new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                String message = _entry.getText().trim();
                if (message.length() == 0) return;
                _roomobj.roomSvc.get().sendMessage(message).
                    bindComplete(new Slot<Boolean>() { public void onEmit (Boolean isComplete) {
                        _entry.setEnabled(isComplete);
                        send.setEnabled(isComplete);
                    }}).
                    onSuccess(new UnitSlot() { public void onEmit () {
                        _entry.setText("");
                        _entry.requestFocusInWindow();
                    }}).
                    onFailure(reportFailure("Chat send failed"));
            }
        };
        send.addActionListener(sender);
        _entry.addActionListener(sender);

        // make a request for the current room list
        refreshRooms.onEmit();
    }

    protected final UnitSlot refreshRooms = new UnitSlot() { public void onEmit () {
        _chatobj.chatSvc.get().getRooms().onSuccess(new Slot<List<String>>() {
            public void onEmit (final List<String> rooms) {
                _rooms.setModel(new AbstractListModel() {
                    public int getSize () { return rooms.size(); }
                    public Object getElementAt (int idx) { return rooms.get(idx); }
                });
            }
        }).onFailure(reportFailure("Failed to fetch rooms"));
    }};

    protected void updateNickname (final String nickname) {
        if (nickname.length() == 0) {
            feedback("Error: can't use blank nickname");
        } else {
            _chatobj.chatSvc.get().updateNick(nickname).onSuccess(new UnitSlot() {
                public void onEmit () { feedback("Nickname updated to '" + nickname + "'."); }
            }).onFailure(reportFailure("Failed to update nickname"));
        }
    }

    protected void joinRoom (final String name) {
        if (_roomobj != null && _roomobj.name.equals(name)) {
            return; // no point in noopin'
        }
        if (_sub != null) _sub.unsubscribe();
        _chatobj.chatSvc.get().joinRoom(name).
            flatMap(_sub = _ctx.getClient().subscriber()).
            onSuccess(joinedRoom).
            onFailure(reportFailure("Failed to join room '" + name + "'"));
    }

    protected void createRoom (final String name) {
        if (_sub != null) _sub.unsubscribe();
        _chatobj.chatSvc.get().createRoom(name).
            flatMap(_sub = _ctx.getClient().subscriber()).
            onSuccess(joinedRoom.andThen(refreshRooms)).
            onFailure(reportFailure("Failed to create room '" + name + "'"));
    }

    protected final Slot<RoomObject> joinedRoom = new Slot<RoomObject>() {
        public void onEmit (RoomObject room) {
            _roomobj = room;
            _roomobj.chatEvent.connect(new Slot<RoomObject.ChatEvent>() {
                public void onEmit (RoomObject.ChatEvent event) {
                    if (event.nickname == null) {
                        _chat.append(event.message + "\n"); // from the server
                    } else {
                        _chat.append("<" + event.nickname + "> " + event.message + "\n");
                    }
                }
            });
            _curRoom.setText("Room: " + room.name);
            feedback("Joined room '" + room.name + "'");
            _entry.setEnabled(true);
            _entry.requestFocusInWindow();
        }
    };

    protected void feedback (String message) {
        _chat.append(message + "\n");
    }

    protected Slot<Throwable> reportFailure (final String errpre) {
        return new Slot<Throwable>() { public void onEmit (Throwable cause) {
            feedback(errpre + ": " + cause.getMessage());
        }};
    }

    protected ChatContext _ctx;
    protected ChatObject _chatobj;
    protected RoomObject _roomobj;
    protected Subscriber<RoomObject> _sub;

    protected JList _rooms;
    protected JLabel _curRoom;
    protected JTextArea _chat;
    protected JTextField _entry;
}
