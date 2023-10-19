import Button from "./Button";

function GameButtons(props){

    return(
        <div className="GameButtons">

            <Button onClick={props.onGameButton} type="ingame" name="Hint"/>
            <Button onClick={props.onGameButton} type="ingame" name="Solve"/>
            <Button onClick={props.onGameButton} type="break" name="Reset"/>
            <Button onClick={props.onGameButton} type="submit" name="Submit"/>

        </div>
    );

}

export default GameButtons;