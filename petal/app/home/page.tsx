import NavBar from "@/components/navigation/NavBar";

export default function HomePage() {


  //  RENDER

  return (
    <div className={""}>
      <NavBar variant={'primary'} />
      <NavBar variant={'secondary'} />
      <NavBar variant={'tertiary'} />
      <NavBar variant={'white'} />
    </div>
  )
}