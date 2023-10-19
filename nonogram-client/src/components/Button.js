function Button(props){

    const styleButton = "Button Button__"+props.type;

    const click = ()=>{
        props.onClick(props.name);
    }

    return (
        <>
        <button onClick={click} className={styleButton}>{props.name}</button>
        </>
    );

}

export default Button;