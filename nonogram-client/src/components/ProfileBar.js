import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckSquare, faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';

function ProfileBar(props){

    const click = ()=>{
        props.onExit();
    }

    return(
        <div className="ProfileBar">
            <div className="ProfileBar__profile">
                <div className="ProfileBar__profile__icon">{props.firstAlph}</div>
                <div className="ProfileBar__profile__username">{props.username}</div>
            </div>
            <div onClick={click} className="ProfileBar__settings"><FontAwesomeIcon icon={faArrowRightFromBracket} /></div>
        </div>
        );

}

export default ProfileBar;