import GameNameBar from "./GameNameBar";
import ProfileBar from "./ProfileBar";
import StateBar from "./StateBar";
import MENU_OPTIONS from "../utils/menu";

function Header(props){

    let stateName = Object.keys(MENU_OPTIONS)[props.stateName];
    

    return(
    <div className="Header">
        <GameNameBar/>
        <StateBar stateName={stateName}/>
        <ProfileBar onExit={props.onExit} firstAlph={props.username.charAt(0).toUpperCase()} username={props.username}/>
    </div>
    );

}

export default Header;