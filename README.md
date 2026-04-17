# snake

Download Main.java and run this command in a terminal. You need Java 1.6 or higher.

Change ~/Downloads if u need.

RUTA=$( [ -d ~/Downloads ] && echo ~/Downloads || echo ~/Descargas ); \
( command -v gnome-terminal >/dev/null && gnome-terminal --geometry=40x25 -- bash -c "cd \"$RUTA\" && java Main.java; exec bash" ) \
|| ( command -v xfce4-terminal >/dev/null && xfce4-terminal --geometry=40x25 --command="bash -c 'cd \"$RUTA\" && java Main.java; exec bash'" ) \
|| ( command -v konsole >/dev/null && konsole --geometry 40x25 --hold -e bash -c "cd \"$RUTA\" && java Main.java" ) \
|| ( command -v mate-terminal >/dev/null && mate-terminal --geometry=40x25 -- bash -c "cd \"$RUTA\" && java Main.java; exec bash" ) \
|| ( command -v tilix >/dev/null && tilix --geometry=40x25 -e "bash -c 'cd \"$RUTA\" && java Main.java; exec bash'" ) \
|| ( command -v terminator >/dev/null && terminator --geometry=40x25 -x bash -c "cd \"$RUTA\" && java Main.java; exec bash" ) \
|| ( command -v xterm >/dev/null && xterm -geometry 40x25 -e "cd \"$RUTA\" && java Main.java" )
