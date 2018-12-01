# durak
Project for course: Mobile and distributed systems

## Core library
All HTTP responses from the server lies under *.core.net, as well as POST body messages.
For reference:

net.
* lobby.
* * LobbyItemInfo -- meta info for a lobby
* * LobbyItemInfoList -- wraps a List&lt;LobbyItemInfo&gt;
* * LobbyState -- details of a lobby, including player names
* match.
* * Action -- a player action, such as "take cards" or "use card"
* * MatchClientState -- the match state for a specific client, i.e. what the player actually can see
* * MatchSnapshot -- wraps a List&lt;MatchClientState&gt;
* * FinishedMatch -- wraps a List&lt;MatchSnapshot&gt;
* * FinishedMatchItemInfo -- meta info for a finished match that can be played back
* * FinishedMatchItemInfoList -- wraps a List&lt;FinishedMatchItemInfo&gt;
* player.
* * Leaderboard -- a list of players with their victories/defeats and ratio
* * LoginAttempt -- Google token id, Google account id and display name
* * * This should maybe be renamed to "GoogleLoginAttempt"


## Client (Android)
Fragments are based on MainActivityFragment, which makes it a bit easier to reach the main activity, and also adds a neat showAlert() method. Each fragment overrides onStart() to specify whether or not it wants the navigation at the bottom hidden or shown. This takes away the responsibility of keeping track of when to show or hide it.

### Login (LoginFragment)
When first starting the app, you will be asked to sign in with your Google account. This will then be remembered for future launches of the app. 

### Lobby selection (LobbyTableFragment)
Once logged in, you may view currently open lobbies created by other players, or you can open your own. 

### Match lobby (LobbyFragment)
When you have joined a lobby, the match will begin when any of you press start, assuming there are enough players.

### Live match (MatchFragment)
Each player takes turn being the attacker or defender. Currently, only two players may play in one match, and the main reason for this is lack of testing opportunities. The code mostly supports 3+ players, except a few cases such as attacker and defender currently is just being swapped when a turn ends.

#### Surface view
This is initialized by the fragment, and sets the GL context, as well as creating the renderer. It listens to touch events, and keeps track of where a touch begins, moves and ends. This information is sent to the match controller.

#### Match controller
Contains some controllers that respond to touch events and/or manages transforms for the renderer.
* MatchClient -- contains the state of the match, and sends actions to the server (called by controllers)
* MyHandController -- checks if a card is being dragged up, indicating the player wants to use it
* OtherHandController -- only controls transforms
* BoutController -- checks if cards are being dragged down, indicating the player wants to take them

#### State controller
There are two state controllers, extending an abstract StateController class.
* LiveStateController -- polls the server every 2 seconds
* PlaybackStateController -- loads a finished match from the server, and imitates the controller above, but it also provides a next() method to let the user skip a snapshot by tapping

#### Renderer
Creates shader program, camera, and all the renderers:
* MyHandRenderer -- draws your cards
* OtherHandRenderer -- draws the backside of the opponents cards (based on the number of cards)
* TalonRenderer -- simply draws the stack of cards upside down and slightly rotated, with the trumping card visible at 90 degrees rotation on the bottom
* BoutRenderer -- draws attacking and defending cards in the current bout
* InfoDisplay -- shows helpful text in top part of screen
* BackgroundRenderer

The renderer updates the match controller, which seems to be unavoidable with how EGL works. Of course, it is possible to create a separate thread, but there is no practical reason for that, and very uncommon.

### Leaderboard (LeaderboardFragment)
See the statistics for the top 100 players. Currently sorted only by ratio.

### Global match history to select for playback (RecordedMatchTableFragment)
After a live match has been fully or partially completed, it can be played back by selecting it in the rightmost tab. All matches are visible in this tab, and not only the ones played by you.

### Match playback (MatchPlaybackFragment)
While the match is playing back, each player action will be separated by 2 seconds. This can be avoided by tapping once for each action.

### Table row builder
There is a simple TableRowBuilder class to allow for building tables quicker, and is used by both LobbyTableFragment and RecordedMatchTableFragment.


## Server (Payara)
### Consists of four services:
* PlayerService
* * GET leaderboard -> net.player.Leaderboard
* * POST rename -> Boolean
* * POST login -> Boolean
* LobbyService
* * GET list -> net.lobby.LobbyItemInfoList
* * POST create -> Boolean
* * POST join -> Boolean
* * GET view -> net.lobby.LobbyState
* * POST start -> Boolean 
* MatchService
* * GET state -> net.match.MatchClientState
* * GET action -> net.match.Action 
* HistoryService
* * GET list -> net.match.FinishedMatchInfoList
* * GET match(id) -> net.match.FinishedMatch

### Entities are:
* MatchEntity (match)
* * Stores a FinishedMatch as JSON in a text field
* PlayerEntity (player)
* * Statistics such as victories, defeats and ratio

