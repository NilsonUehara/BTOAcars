VERSION 5.00
Begin VB.Form Form1 
   Caption         =   "Form1"
   ClientHeight    =   3030
   ClientLeft      =   120
   ClientTop       =   450
   ClientWidth     =   4560
   LinkTopic       =   "Form1"
   ScaleHeight     =   3030
   ScaleWidth      =   4560
   StartUpPosition =   3  'Windows Default
   Begin VB.CommandButton Command1 
      Caption         =   "Command1"
      Height          =   540
      Left            =   1680
      TabIndex        =   0
      Top             =   1260
      Width           =   1170
   End
End
Attribute VB_Name = "Form1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub Command1_Click()
Dim Conn As New ADODB.Connection
Dim Rs As New ADODB.Recordset
Dim Rs2 As New ADODB.Recordset
Dim Sql As String
Dim pula As Boolean

Conn.Open "Driver={Mysql Driver};Server=aeroboteco.com.br;Database=union842_bto;Uid=union842_usrbto;Pwd=Bto51Boteco;Option=3;"

Sql = "select pilot_id, origin_id, destination_id, equipment, duration, fuel, distance"
Sql = Sql & " from union842_bto.reports"
Sql = Sql & " Where Date > '2009-01-01'"
Sql = Sql & " group by pilot_id, origin_id, destination_id, equipment, duration, fuel, distance"
Sql = Sql & " having count(*) > 1"
Sql = Sql & " order by pilot_id, origin_id, destination_id, equipment, duration, fuel, distance;"
Rs.Open Sql, Conn, adOpenStatic, adLockReadOnly

While Not Rs.EOF
    Sql = "select *"
    Sql = Sql & " from union842_bto.reports"
    Sql = Sql & " Where pilot_id = '" & Rs!pilot_id & "'"
    Sql = Sql & " and origin_id='" & Rs!origin_id & "'"
    Sql = Sql & " and destination_id='" & Rs!destination_id & "'"
    Sql = Sql & " and equipment='" & Rs!equipment & "'"
    Sql = Sql & " and duration='" & Format(Rs!duration, "hh:mm:ss") & "'"
    Sql = Sql & " and fuel='" & Rs!fuel & "'"
    Sql = Sql & " and distance='" & Rs!distance & "'"
    Sql = Sql & " order by report_id desc"
    Rs2.Open Sql, Conn, adOpenDynamic, adLockOptimistic
    pula = True
    While Not Rs2.EOF
        Caption = Rs2!report_id & " - " & Rs2!callsign
        DoEvents
        If Not pula Then
            Rs2.Delete
        End If
        pula = False
        Rs2.MoveNext
    Wend
    Rs2.Close
    Rs.MoveNext
Wend
Rs.Close
Set Rs = Nothing
Set Rs2 = Nothing
Conn.Close
Set Conn = Nothing
End

End Sub
