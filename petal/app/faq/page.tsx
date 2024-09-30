import AdminBar from "@/components/navigation/AdminBar";
import SimpleHero from "@/components/hero/SimpleHero";
import NavBar from "@/components/navigation/NavBar";

export default function FAQPage() {


  //  RENDER

  return (
    <div>
      <AdminBar variant={'tertiary'} />
      <NavBar variant={'white'} />
      <SimpleHero
        variant={'tertiary'}
        title={'Frequently Asked Questions'}
        subtitle={'Some commonly asked questions and concerns that we hope to alleviate before anything!'}
        size={'small'}
        position={"left"}
        alignment={"left"}
        highlight={false}
        hasNavBar={false}
      />
      HELLO WORLD!
    </div>
  )
}